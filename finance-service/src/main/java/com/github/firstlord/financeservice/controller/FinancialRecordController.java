package com.github.firstlord.financeservice.controller;

import com.github.firstlord.financeservice.dto.category.CategoryStatsDTO;
import com.github.firstlord.financeservice.dto.financialRecord.FinancialRecordCreateDTO;
import com.github.firstlord.financeservice.dto.financialRecord.FinancialRecordDetailDTO;
import com.github.firstlord.financeservice.dto.financialRecord.FinancialRecordShortDTO;
import com.github.firstlord.financeservice.dto.financialRecord.FinancialRecordUpdateDTO;
import com.github.firstlord.financeservice.service.FinancialRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {
    @Autowired
    private FinancialRecordService recordService;
    // создаем новую запись
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRecord(@RequestBody FinancialRecordCreateDTO dto,
                                       @RequestHeader("X-User-Id") String userId) {
        try {
            if (userId == null || userId.isEmpty()) {
                return new ResponseEntity("userId is empty", HttpStatus.BAD_REQUEST);
            }
            FinancialRecordDetailDTO result = recordService.create(dto, userId);
            return new ResponseEntity(result, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // записи юзера
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllRecords(@RequestHeader("X-User-Id") String userId) {
        try {
            List<FinancialRecordShortDTO> list = recordService.getAll(userId);
            return new ResponseEntity(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // получаем одну запись по id
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneRecord(@PathVariable("id") UUID id,
                                       @RequestHeader("X-User-Id") String userId) {
        try {
            FinancialRecordDetailDTO dto = recordService.getById(id, userId);
            return new ResponseEntity(dto, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error in getOneRecord: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateRecord(@PathVariable("id") UUID id,
                                       @RequestBody FinancialRecordUpdateDTO dto,
                                       @RequestHeader("X-User-Id") String userId) {
        try {
            FinancialRecordDetailDTO result = recordService.update(id, dto, userId);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteRecord(@PathVariable("id") UUID id,
                                       @RequestHeader("X-User-Id") String userId) {
        try {
            recordService.delete(id, userId);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // получаем записи по конкретной категории
    @RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity getRecordsByCategory(@PathVariable("categoryId") UUID categoryId,
                                               @RequestHeader("X-User-Id") String userId) {
        try {
            List<FinancialRecordShortDTO> list = recordService.getByCategory(categoryId, userId);
            return new ResponseEntity(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // статистика по категориям за период
    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public ResponseEntity getStats(@RequestHeader("X-User-Id") String userId,
                                   @RequestParam("start") String start,
                                   @RequestParam("end") String end) {
        try {
            // парсим даты вручную, потому что так проще понять
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            List<CategoryStatsDTO> stats = recordService.getCategoryStats(userId, startDate, endDate);
            return new ResponseEntity(stats, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("wrong date format", HttpStatus.BAD_REQUEST);
        }
    }
}