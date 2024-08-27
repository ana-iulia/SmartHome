package com.app.smart_home.controller;


import com.app.smart_home.domain.dto.DeviceDTO;
import com.app.smart_home.domain.dto.DeviceStartDTO;
import com.app.smart_home.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/smart-home")
@RequiredArgsConstructor
public class DeviceManagementController {


    @Autowired
    private DeviceService deviceService;


    @PostMapping("/device")
    public ResponseEntity<DeviceDTO> createDevice(@RequestBody DeviceDTO deviceDTO) {
        return ResponseEntity.ok().body(deviceService.saveDevice(deviceDTO));
    }

    @PutMapping("/device/start")
    public ResponseEntity<String> startDevice(@RequestBody DeviceStartDTO deviceStart) {
        deviceService.startDevice(deviceStart);
        return ResponseEntity.ok().body("Device with serialId " + deviceStart.getSerialId() + "started consuming energy");
    }


//    @PutMapping("/update/{taskTitle}")
//    public ResponseEntity<TaskDTO> updateTask(@RequestBody @Valid TaskDTO taskDto, @PathVariable("taskTitle") String taskTitle) {
//        return ResponseEntity.ok().body(taskService.updateTask(taskDto, taskTitle));
//    }

//    @GetMapping
//    public ResponseEntity<List<TaskDTO>> getAllTasks() {
//        return ResponseEntity.ok().body(taskService.getAllTasks());
//    }
//
//    @GetMapping("/{userEmail}/{projectName}")
//    public ResponseEntity<List<TaskDTO>> getAllTasksByUserProject(@PathVariable("userEmail") String userEmail, @PathVariable("projectName") String projectName) {
//        return ResponseEntity.ok().body(taskService.getAllTasksByUserProject(userEmail, projectName));
//    }
//
//
//    @GetMapping("/unassigned/{projectName}")
//    public ResponseEntity<List<TaskDTO>> getAllTasksUnassignedProject(@PathVariable("projectName") String projectName) {
//        return ResponseEntity.ok().body(taskService.getAllTasksUnassignedProject(projectName));
//    }


}
