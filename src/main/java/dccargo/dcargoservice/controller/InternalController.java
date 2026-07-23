package dccargo.dcargoservice.controller;

import dccargo.dcargoservice.service.dcargo.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor  // генерирует конструктор для всех final полей
@RequestMapping("/internal")
public class InternalController {

    private final OrderService orderService;





}
