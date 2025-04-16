package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.NotifyDTO;
import com.taekang.employeeservletapi.DTO.NotifyWithReadDTO;
import com.taekang.employeeservletapi.entity.employee.Level;
import com.taekang.employeeservletapi.entity.employee.Notify;
import java.util.List;

public interface NotifyService {

  Notify createNotify(NotifyDTO notifyDTO);

  Notify updateNotify(NotifyDTO notifyDTO);

  List<Notify> getAllNotifylist();

  void deleteNotify(Long notifyId);

  Notify getNotifyById(Long notifyId);

  Notify getLatestNotify();

  List<Notify> getAllNotifyForLevel(Level level); // level 이하만 반환

  List<NotifyWithReadDTO> getAllNotifyWithReadState(Long employeeId, Level employeeLevel);

  void markAsRead(Long notifyId, Long employeeId);

  boolean isNotifyRead(Long notifyId, Long employeeId);

  long countUnreadNotify(Long employeeId, Level employeeLevel);

  List<Notify> getReadNotifyListByEmployee(Long employeeId);

  List<Notify> getUnreadNotifyListByEmployee(Long employeeId, Level employeeLevel);

  List<Notify> getAllUnreadNotifyListByEmployee(Long employeeId, Level employeeLevel);
}
