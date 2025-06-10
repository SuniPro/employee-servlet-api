package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.*;
import com.taekang.employeeservletapi.entity.employee.*;
import com.taekang.employeeservletapi.error.AbilityNotFoundException;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.repository.employee.AbilityRepository;
import com.taekang.employeeservletapi.repository.employee.CommuteRepository;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import com.taekang.employeeservletapi.service.ReviewService;
import java.time.*;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReviewServiceImplements implements ReviewService {

  private final CommuteRepository commuteRepository;
  private final AbilityRepository abilityRepository;
  private final EmployeeRepository employeeRepository;

  private static final EnumSet<Level> MANAGER_GROUP =
      EnumSet.of(Level.CEO, Level.COO, Level.CFO, Level.CIO, Level.CDO, Level.CTO, Level.MANAGER);

  @Autowired
  public ReviewServiceImplements(
      CommuteRepository commuteRepository,
      AbilityRepository abilityRepository,
      EmployeeRepository employeeRepository) {
    this.commuteRepository = commuteRepository;
    this.abilityRepository = abilityRepository;
    this.employeeRepository = employeeRepository;
  }

  @Override
  public void workOn(CommuteDTO commuteDTO) {
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.atTime(LocalTime.MAX);

    if (commuteRepository.existsByEmployeeAndOnTimeBetween(commuteDTO.getEmployee(), start, end)) {
      LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1);
      log.info("{}님 출근 처리: {}", commuteDTO.getEmployee().getName(), now);

      Commute commute = Commute.builder().employee(commuteDTO.getEmployee()).onTime(now).build();
      commuteRepository.save(commute);
    }
  }

  @Override
  public void workOff(CommuteDTO commuteDTO) {
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Singapore"));
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.atTime(LocalTime.MAX);

    if (commuteRepository.existsByEmployeeAndOffTimeBetween(commuteDTO.getEmployee(), start, end)) {
      LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1);
      log.info("{}님 퇴근 처리: {}", commuteDTO.getEmployee().getName(), now);

      Commute commute = Commute.builder().employee(commuteDTO.getEmployee()).offTime(now).build();
      commuteRepository.save(commute);

      Commute comm = commuteRepository.findCommuteByOffTimeBetween(start, end);

      int workTime = comm.getOnTime().getHour() - comm.getOffTime().getHour();
      int attitudeValue;

      if (workTime < 8) {
        attitudeValue = 1;
      } else if (workTime < 10) {
        attitudeValue = 2;
      } else if (workTime < 13) {
        attitudeValue = 3;
      } else if (workTime < 15) {
        attitudeValue = 4;
      } else {
        attitudeValue = 5;
      }

      Ability ability = Ability.builder().attitude(attitudeValue).build();
      abilityRepository.save(ability);
    }
  }

  @Override
  public Page<AbilityReviewDTO> getEmployeesAbilityReviews(
      Level level, Department department, Pageable pageable) {
    int rank = level.getRank();
    long totalCount = employeeRepository.countByLevelRankLessThan(rank);
    int limit = pageable.getPageSize();

    // OFFSET 보정
    long offset = pageable.getOffset();
    if (offset >= totalCount) {
      int lastPage = (int) Math.ceil((double) totalCount / limit) - 1;
      offset = Math.max(0, lastPage * limit);
    }

    List<Employee> employeeList = employeeRepository.findByLevelRankLessThan(rank, limit, offset);

    List<Long> employeeIds =
        employeeList.stream().map(Employee::getId).collect(Collectors.toList());
    Map<Long, List<Ability>> abilitiesMap =
        abilityRepository.findByEmployeeIdInAndReviewDate(employeeIds, LocalDate.now()).stream()
            .collect(Collectors.groupingBy(ability -> ability.getEmployee().getId()));

    List<AbilityReviewDTO> content =
        employeeList.stream()
            .map(
                employee -> {
                  List<Ability> abilityList =
                      abilitiesMap.getOrDefault(employee.getId(), Collections.emptyList());

                  return AbilityReviewDTO.builder()
                      .employeeId(employee.getId())
                      .employeeName(employee.getName())
                      .creativity(
                          abilityList.isEmpty()
                              ? 3.0
                              : abilityList.stream()
                                  .collect(Collectors.averagingDouble(Ability::getCreativity)))
                      .workPerformance(
                          abilityList.isEmpty()
                              ? 3.0
                              : abilityList.stream()
                                  .collect(Collectors.averagingDouble(Ability::getWorkPerformance)))
                      .teamwork(
                          abilityList.isEmpty()
                              ? 3.0
                              : abilityList.stream()
                                  .collect(Collectors.averagingDouble(Ability::getTeamwork)))
                      .knowledgeLevel(
                          abilityList.isEmpty()
                              ? 3.0
                              : abilityList.stream()
                                  .collect(Collectors.averagingDouble(Ability::getKnowledgeLevel)))
                      .reviewDate(
                          abilityList.isEmpty()
                              ? null
                              : abilityList.stream()
                                  .map(Ability::getReviewDate)
                                  .max(LocalDate::compareTo) // 가장 최근 날짜
                                  .orElse(null))
                      .build();
                })
            .collect(Collectors.toList());

    return new PageImpl<>(content, pageable, totalCount);
  }

  @Override
  public List<WorkBalanceDTO> getWorkBalanceByEmployeeId(Long employeeId) {
    LocalDateTime minus3Month = LocalDateTime.now().minusMonths(3);
    List<Commute> commutes =
        commuteRepository.findCommuteByEmployee_IdAndOnTimeBetween(
            employeeId, minus3Month, LocalDateTime.now());

    return commutes.stream()
        .map(
            commute -> {
              Duration duration = Duration.between(commute.getOnTime(), commute.getOffTime());
              Integer workTime = Math.toIntExact(duration.toHours());

              return WorkBalanceDTO.builder()
                  .employeeId(employeeId)
                  .workBalance(workTime)
                  .date(commute.getOnTime())
                  .build();
            })
        .toList();
  }

  @Override
  public void abilityReview(AbilityDTO abilityDTO) {
    LocalDate now = LocalDate.now(ZoneId.of("Asia/Singapore"));

    Ability ability =
        Ability.builder()
            .id(abilityDTO.getId())
            .creativity(abilityDTO.getCreativity())
            .teamwork(abilityDTO.getTeamwork())
            .workPerformance(abilityDTO.getWorkPerformance())
            .knowledgeLevel(abilityDTO.getKnowledgeLevel())
            .reviewDate(now)
            .build();

    abilityRepository.save(ability);
  }

  @Override
  public EmployeesAbilityDTO getEmployeeAbility(Level level, Long employeeId) {
    Employee targetEmployee =
        employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);

    EmployeesAbilityDTO employeesAbilityDTO = new EmployeesAbilityDTO();

    List<Employee> anotherEmployees;

    if (MANAGER_GROUP.contains(level)) {
      anotherEmployees =
          employeeRepository.findAll().stream()
              .filter(
                  emp ->
                      !MANAGER_GROUP.contains(emp.getLevel())
                          && emp.getLevel() != Level.OFFICEMANAGER)
              .collect(Collectors.toList());
    } else if (level == Level.OFFICEMANAGER) {
      anotherEmployees =
          employeeRepository.findByDepartment(targetEmployee.getDepartment()).stream()
              .filter(emp -> emp.getLevel() != Level.OFFICEMANAGER)
              .collect(Collectors.toList());
    } else {
      /* 사원이 조회할 경우에도 OFFICE_MANAGER 와 동일하게 자신과 같은 부서의 데이터를 가져옵니다. */
      anotherEmployees =
          employeeRepository.findByDepartment(targetEmployee.getDepartment()).stream()
              .filter(emp -> emp.getLevel() != Level.OFFICEMANAGER)
              .collect(Collectors.toList());
    }

    List<Ability> employeeAbility = abilityRepository.findByEmployee_Id(targetEmployee.getId());
    employeesAbilityDTO.setEmployeeId(employeeId);
    employeesAbilityDTO.setAbility(getAverageAbilityInfo(employeeAbility));
    employeesAbilityDTO.setEmployeesAbilityList(
        getAdjustedAbilitiesByEmployeeList(anotherEmployees));

    return employeesAbilityDTO;
  }

  @Override
  public AbilityDTO getAbilityByEmployeeId(Long employeeId) {

    List<Ability> abilities = abilityRepository.findByEmployee_Id(employeeId);

    if (abilities.isEmpty()) {
      throw new AbilityNotFoundException();
    }

    return getAverageAbilityInfo(abilities);
  }

  @Override
  public List<String> createAbility(List<AbilityReviewDTO> abilityReviewDTOList) {
    List<Ability> abilityList = new ArrayList<>();
    List<String> successList = new ArrayList<>();

    abilityReviewDTOList.forEach(
        abilityReviewDTO -> {
          Ability ability =
              Ability.builder()
                  .employee(Employee.builder().id(abilityReviewDTO.getEmployeeId()).build())
                  .creativity(abilityReviewDTO.getCreativity())
                  .workPerformance(abilityReviewDTO.getWorkPerformance())
                  .teamwork(abilityReviewDTO.getTeamwork())
                  .knowledgeLevel(abilityReviewDTO.getKnowledgeLevel())
                  .reviewDate(LocalDate.now(ZoneId.of("Asia/Singapore")))
                  .build();

          successList.add(abilityReviewDTO.getEmployeeName());
          abilityList.add(ability);
        });

    abilityRepository.saveAll(abilityList);
    return successList;
  }

  public List<AbilityDTO> getAdjustedAbilitiesByEmployeeList(List<Employee> employees) {
    if (employees.isEmpty()) return List.of();
    return employees.stream()
        .map(
            employee -> {
              List<Ability> abilities = abilityRepository.findByEmployee(employee);
              return getAverageAbilityInfo(abilities);
            })
        .toList();
  }

  private AbilityDTO getAverageAbilityInfo(List<Ability> abilities) {
    if (abilities.isEmpty()) return AbilityDTO.builder().build();
    return AbilityDTO.builder()
        .id(abilities.get(0).getId())
        .employee(abilities.get(0).getEmployee())
        .attitude(adjustedWeightedAverage(abilities, Ability::getAttitude))
        .teamwork(adjustedWeightedAverage(abilities, Ability::getTeamwork))
        .creativity(adjustedWeightedAverage(abilities, Ability::getCreativity))
        .workPerformance(adjustedWeightedAverage(abilities, Ability::getWorkPerformance))
        .knowledgeLevel(adjustedWeightedAverage(abilities, Ability::getKnowledgeLevel))
        .build();
  }

  private double adjustedWeightedAverage(
      List<Ability> abilities, ToDoubleFunction<Ability> getter) {
    int count = abilities.size();
    double sum = abilities.stream().mapToDouble(getter).sum();
    int PSEUDO_COUNT = 5;
    double GLOBAL_MEAN = 2.7;
    return (sum + GLOBAL_MEAN * PSEUDO_COUNT) / (count + PSEUDO_COUNT);
  }
}
