package dccargo.dcargoservice.model.dcargo;

import java.util.List;

import org.springframework.stereotype.Service;

import dccargo.dcargoservice.repository.dcargo.WorkScheduleRepository;
import dccargo.dcargoservice.service.dcargo.exception.MainServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkScheduleService {

	private final WorkScheduleRepository workScheduleRepository;

    public WorkSchedule create(WorkSchedule workSchedule) {
        validate(workSchedule);

        if (workScheduleRepository.existsByNameIgnoreCase(
                workSchedule.getName().trim())) {

            throw new MainServiceException(
                    "График с названием \""
                            + workSchedule.getName()
                            + "\" уже существует"
            );
        }

        workSchedule.setId(null);
        workSchedule.setName(workSchedule.getName().trim());

        WorkSchedule saved = workScheduleRepository.save(workSchedule);

        log.info(
                "Создан рабочий график: id={}, name={}, workDays={}, restDays={}",
                saved.getId(),
                saved.getName(),
                saved.getWorkDays(),
                saved.getRestDays()
        );

        return saved;
    }

    @Transactional
    public WorkSchedule update(
            Long id,
            WorkSchedule request
    ) {
        WorkSchedule existing = getById(id);

        if (request.getName() != null) {
            String name = request.getName().trim();

            if (name.isBlank()) {
                throw new MainServiceException(
                        "Название графика не может быть пустым"
                );
            }

            if (workScheduleRepository
                    .existsByNameIgnoreCaseAndIdNot(name, id)) {

                throw new MainServiceException(
                        "График с названием \""
                                + name
                                + "\" уже существует"
                );
            }

            existing.setName(name);
        }

        if (request.getWorkDays() != null) {
            validateDays(
                    request.getWorkDays(),
                    "Количество рабочих дней"
            );

            existing.setWorkDays(request.getWorkDays());
        }

        if (request.getRestDays() != null) {
            validateDays(
                    request.getRestDays(),
                    "Количество выходных дней"
            );

            existing.setRestDays(request.getRestDays());
        }

        validateCycleLength(
                existing.getWorkDays(),
                existing.getRestDays()
        );

        log.info("Обновлён рабочий график id={}", id);

        return existing;
    }

    public WorkSchedule getById(Long id) {
        if (id == null) {
            throw new MainServiceException(
                    "Не указан ID рабочего графика"
            );
        }

        return workScheduleRepository.findById(id)
                .orElseThrow(() -> new MainServiceException(
                        "Рабочий график с ID "
                                + id
                                + " не найден"
                ));
    }

    public List<WorkSchedule> getAll() {
        return workScheduleRepository.findAllByOrderByNameAsc();
    }

    public void delete(Long id) {
        WorkSchedule workSchedule = getById(id);

        workScheduleRepository.delete(workSchedule);

        log.info("Удалён рабочий график id={}", id);
    }

    private void validate(WorkSchedule workSchedule) {
        if (workSchedule == null) {
            throw new MainServiceException(
                    "Не переданы данные рабочего графика"
            );
        }

        if (workSchedule.getName() == null
                || workSchedule.getName().isBlank()) {

            throw new MainServiceException(
                    "Не указано название рабочего графика"
            );
        }

        validateDays(
                workSchedule.getWorkDays(),
                "Количество рабочих дней"
        );

        validateDays(
                workSchedule.getRestDays(),
                "Количество выходных дней"
        );

        validateCycleLength(
                workSchedule.getWorkDays(),
                workSchedule.getRestDays()
        );
    }

    private void validateDays(
            Integer days,
            String fieldName
    ) {
        if (days == null || days <= 0) {
            throw new MainServiceException(
                    fieldName + " должно быть больше нуля"
            );
        }
    }

    private void validateCycleLength(
            Integer workDays,
            Integer restDays
    ) {
        if (workDays + restDays > 365) {
            throw new MainServiceException(
                    "Продолжительность цикла не может превышать 365 дней"
            );
        }
    }
}
