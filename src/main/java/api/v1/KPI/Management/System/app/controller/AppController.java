package api.v1.KPI.Management.System.app.controller;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AppController {
    @Autowired
    private AppService appService;

    @GetMapping("/{id}")
    private ResponseEntity<AppResponse<String>> create(@PathVariable String id) {
        return ResponseEntity.ok().body(appService.create(id));
    }
}
