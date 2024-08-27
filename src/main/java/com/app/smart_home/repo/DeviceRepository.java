package com.app.smart_home.repo;


import com.app.smart_home.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
//   List<Task> findAllByUserEmailAndAndProjectName(String userEmail, String projectName);
//   List<Task> findAllByProjectName(String projectName);
//   Task findByTitle(String title);


}
