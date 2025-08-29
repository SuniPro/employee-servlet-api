package com.taekang.employeeservletapi.service.impl;

import com.taekang.employeeservletapi.DTO.NotifyDTO;
import com.taekang.employeeservletapi.DTO.NotifyWithReadDTO;
import com.taekang.employeeservletapi.entity.employee.Employee;
import com.taekang.employeeservletapi.entity.employee.Notify;
import com.taekang.employeeservletapi.entity.employee.NotifyRead;
import com.taekang.employeeservletapi.error.EmployeeNotFoundException;
import com.taekang.employeeservletapi.error.NotifyNotFoundedException;
import com.taekang.employeeservletapi.repository.employee.EmployeeRepository;
import com.taekang.employeeservletapi.repository.employee.NotifyReadRepository;
import com.taekang.employeeservletapi.repository.employee.NotifyRepository;
import com.taekang.employeeservletapi.service.NotifyService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyServiceImplements implements NotifyService {

  private final NotifyRepository notifyRepository;
  private final NotifyReadRepository notifyReadRepository;
  private final EmployeeRepository employeeRepository;

  @Autowired
  public NotifyServiceImplements(
      NotifyRepository notifyRepository,
      NotifyReadRepository notifyReadRepository,
      EmployeeRepository employeeRepository) {
    this.notifyRepository = notifyRepository;
    this.notifyReadRepository = notifyReadRepository;
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Notify createNotify(NotifyDTO notifyDTO) {

    Notify notify =
        notifyRepository.save(
            Notify.builder()
                .title(notifyDTO.getTitle())
                .contents(notifyDTO.getContents())
                .level(notifyDTO.getLevel())
                .writer(notifyDTO.getWriter())
                    .site(notifyDTO.getSite())
                .insertDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
                .build());

    List<Employee> targets = employeeRepository.findByLevelLessThanEqualAndDeleteNameIsNull(notifyDTO.getLevel());

    List<NotifyRead> reads =
        targets.stream()
            .map(emp -> NotifyRead.builder().notify(notify).employee(emp).readTime(null).build())
            .toList();

    notifyReadRepository.saveAll(reads);

    return notify;
  }

  @Override
  public Notify updateNotify(NotifyDTO notifyDTO) {
    Notify notify =
        Notify.builder()
            .id(notifyDTO.getId())
            .title(notifyDTO.getTitle())
            .contents(notifyDTO.getContents())
                .site(notifyDTO.getSite())
            .updateDateTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
            .build();
    return notifyRepository.save(notify);
  }

  @Override
  public List<Notify> getAllNotifylist() {
    return notifyRepository.findAll();
  }

  @Override
  public void deleteNotify(Long notifyId) {
    notifyRepository.deleteById(notifyId);
  }

  @Override
  public Notify getNotifyById(Long notifyId) {
    return notifyRepository.findById(notifyId).orElseThrow(NotifyNotFoundedException::new);
  }

  @Override
  public Notify getLatestNotify() {
    return notifyRepository
        .findTopByOrderByInsertDateTimeDesc()
        .orElseThrow(NotifyNotFoundedException::new);
  }

  @Override
  public List<Notify> getAllNotifyForLevel(String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    return notifyRepository.findByLevelGreaterThanEqualAndSite(employee.getLevel(), employee.getSite());
  }

  @Override
  public List<NotifyWithReadDTO> getAllNotifyWithReadState(String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    Long employeeId = employee.getId();
    // 1. 전체 공지 가져오기 (레벨 이하)
    List<Notify> notifies = notifyRepository.findByLevelGreaterThanEqualAndSite(employee.getLevel(), employee.getSite());

    List<NotifyRead> readList = notifyReadRepository.findByEmployee_Id(employeeId);

    Map<Long, LocalDateTime> readTimeMap =
        readList.stream()
            .collect(Collectors.toMap(r -> r.getNotify().getId(), NotifyRead::getReadTime));

    // 3. 매핑해서 DTO 리턴
    return notifies.stream()
        .map(
            notify ->
                new NotifyWithReadDTO(
                    notify.getId(),
                    notify.getTitle(),
                    notify.getContents(),
                    readTimeMap.containsKey(notify.getId()),
                    readTimeMap.get(notify.getId())))
        .toList();
  }

  @Override
  @Transactional
  public void markAsRead(Long notifyId, String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    Long employeeId = employee.getId();
    // 이미 읽었는지 먼저 확인
    boolean alreadyRead =
        notifyReadRepository.existsByNotify_IdAndEmployee_Id(notifyId, employeeId);
    if (alreadyRead) return;

    // 읽음 처리 (연관 객체를 직접 조회하지 않고 생성)
    NotifyRead notifyRead =
        NotifyRead.builder()
            .notify(Notify.builder().id(notifyId).build()) // 프록시 객체 (조회 X)
            .employee(Employee.builder().id(employeeId).build()) // 프록시 객체
            .readTime(LocalDateTime.now(ZoneId.of("Asia/Singapore")).minusHours(1))
            .build();

    notifyReadRepository.save(notifyRead);
  }

  @Override
  public boolean isNotifyRead(Long notifyId, String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    return notifyReadRepository.existsByNotify_IdAndEmployee_Id(notifyId, employee.getId());
  }

  @Override
  public long countUnreadNotify(String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    return notifyReadRepository.countUnreadNotifyByEmployee(employee.getId(), employee.getLevel(), employee.getSite());
  }

  @Override
  public List<Notify> getReadNotifyListByEmployee(String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    return notifyReadRepository.findReadNotifyListByEmployee(employee.getId(), employee.getLevel(), employee.getSite());
  }

  @Override
  public List<Notify> getUnreadNotifyListByEmployee(String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    return notifyReadRepository.findUnreadNotifyListByEmployee(employee.getId(), employee.getLevel(), employee.getSite());
  }

  @Override
  public List<Notify> getAllUnreadNotifyListByEmployee(String name) {
    Employee employee = employeeRepository.findByNameAndDeleteNameIsNull(name).orElseThrow(EmployeeNotFoundException::new);
    return notifyReadRepository.findUnreadNotifyAllLevelByEmployee(employee.getId(), employee.getSite());
  }
}
