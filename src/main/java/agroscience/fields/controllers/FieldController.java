package agroscience.fields.controllers;

import agroscience.fields.dto.Page;
import agroscience.fields.dto.field.CoordinatesWithFieldId;
import agroscience.fields.dto.field.RequestField;
import agroscience.fields.dto.field.ResponseFieldPreview;
import agroscience.fields.dto.field.ResponseFieldWithCR;
import agroscience.fields.dto.field.ResponseFullField;
import agroscience.fields.mappers.FieldMapper;
import agroscience.fields.security.AuthoriseService;
import agroscience.fields.security.Role;
import agroscience.fields.services.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fields")
@RequiredArgsConstructor
public class FieldController {

  private final FieldService fieldService;
  private final FieldMapper fieldMapper;
  private final AuthoriseService auth;

  @PostMapping
  @Operation(description = "Создание поля")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {@Content(schema = @Schema())})
  })
  public ResponseFieldPreview createField(@Valid @RequestBody RequestField fieldRequest, HttpServletRequest header) {
    return fieldMapper.mapToFieldReview(fieldService.createField(fieldMapper.map(fieldRequest,
            auth.doFilter(header, new Role.Builder().worker().organization().build()))));
  }

  @GetMapping
  @Operation(description = "Получение поля с полной информацией")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseFullField getField(Long fieldId, HttpServletRequest header) {
    var orgId = auth.doFilter(header, new Role.Builder().worker().organization().build());
    return fieldService.getFullField(fieldId, orgId);
  }

  @GetMapping("/organization")
  @Operation(description = "Получение полей организации с текущими севооборотами")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public List<ResponseFieldWithCR> getFields(@Valid Page page, HttpServletRequest header) {
    return fieldService.getFields(auth.doFilter(header, new Role.Builder().worker().organization().build()),
            PageRequest.of(page.getPage(), page.getSize())).stream().map(fieldMapper::mapToFieldWithCR).toList();
  }

  @GetMapping("preview")
  @Operation(description = "Получение поля вместе с растущей на ней культурой(не севооборот, а именно культура)")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseFieldPreview getFieldPreview(@Valid @Min(1) Long fieldId, HttpServletRequest header) {
    return fieldMapper.mapToFieldReview(fieldService.getFieldWithCurrentCrop(fieldId, auth.doFilter(header,
            new Role.Builder().worker().organization().build())));
  }

  @PutMapping
  @Operation(description = "Обновление поля")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseFieldPreview updateField(@Valid @Min(1) Long fieldId,
                                          @Valid @RequestBody RequestField request,
                                          HttpServletRequest header) {
    return fieldMapper.mapToFieldReview(fieldService.updateField(fieldId, request,
                    auth.doFilter(header, new Role.Builder().organization().worker().build())
            )
    );
  }

  @DeleteMapping
  @Operation(description = "Удаление поля с концами, севообороты этого поля тоже удалятся из базы данных")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseEntity<Void> deleteField(@Valid @Min(1) Long fieldId, HttpServletRequest header) {
    fieldService.deleteField(fieldId, auth.doFilter(header, new Role.Builder().worker().organization().build()));
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/organization/preview")
  @Operation(description = "Получение полей организации вместе с растущими"
          + " на них культурами(не севообороты, а именно культуры)")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public List<ResponseFieldPreview> getFieldsPreview(@Valid Page page, HttpServletRequest header) {
    return fieldService.getFieldsForPreview(
                    auth.doFilter(header, new Role.Builder().worker().organization().build()),
                    PageRequest.of(page.getPage(), page.getSize())
            )
            .stream().map(fieldMapper::mapToFieldReview)
            .toList();
  }

  @GetMapping("/meteo/all-coordinates")
  @Operation(description = "Получение середин всех контуров")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public List<CoordinatesWithFieldId> getAllCoordinates() {
    return fieldService.getAllCoordinates();
  }

}