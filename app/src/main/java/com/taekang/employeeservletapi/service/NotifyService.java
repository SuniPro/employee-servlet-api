package com.taekang.employeeservletapi.service;

import com.taekang.employeeservletapi.DTO.NotifyDTO;
import com.taekang.employeeservletapi.DTO.NotifyWithReadDTO;
import com.taekang.employeeservletapi.entity.employee.Notify;
import java.util.List;

public interface NotifyService {

  Notify createNotify(NotifyDTO notifyDTO);

  Notify updateNotify(NotifyDTO notifyDTO);

  List<Notify> getAllNotifylist();

  void deleteNotify(Long notifyId);

  Notify getNotifyById(Long notifyId);

  Notify getLatestNotify();

  List<Notify> getAllNotifyForLevel(String name); // level 이하만 반환

  List<NotifyWithReadDTO> getAllNotifyWithReadState(String name);

  void markAsRead(Long notifyId, String name);

  boolean isNotifyRead(Long notifyId, String name);

  long countUnreadNotify(String name);

  List<Notify> getReadNotifyListByEmployee(String name);

  List<Notify> getUnreadNotifyListByEmployee(String name);

  List<Notify> getAllUnreadNotifyListByEmployee(String name);
}
