package agroscience.fields.controllers;

import agroscience.fields.dto.soil.RequestSoil;
import agroscience.fields.dto.soil.RequestSoilWithouFieldId;
import agroscience.fields.dto.soil.ResponseSoil;
import agroscience.fields.mappers.SoilMapper;
import agroscience.fields.security.AuthoriseService;
import agroscience.fields.security.Role;
import agroscience.fields.services.SoilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/fields/soil")
public class SoilController {

  private final SoilService soilService;
  private final SoilMapper soilMapper;
  private final AuthoriseService auth;

  @PostMapping
  @Operation(description = "Создание агрохимии")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {@Content(schema = @Schema())})
  })
  public ResponseSoil createSoil(@Valid @RequestBody RequestSoil request, HttpServletRequest header) {
    var orgId = auth.doFilter(header, new Role.Builder().organization().worker().build());
    var soil = soilMapper.map(request);
    return soilMapper.map(soilService.createSoil(orgId, soil, request.getFieldId()));
  }

  @PutMapping
  @Operation(description = "Обновление агрохимии, fieldId фиктивный в request,"
          + " он не меняется, было лень использовать другую dto")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "403", description = "Ошибка авторизации", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {@Content(schema = @Schema())})
  })
  public ResponseSoil updateSoil(
          @Valid @Min(1) Long soilId,
          @Valid @RequestBody RequestSoilWithouFieldId request,
          HttpServletRequest header
  ) {
    var orgId = auth.doFilter(header, new Role.Builder().worker().organization().build());
    return soilMapper.map(soilService.updateSoil(orgId, soilId, soilMapper.map(request)));
  }

  @DeleteMapping
  @Operation(description = "Удаление агрохимии")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseEntity<Void> deleteSoil(@Valid @Min(1) Long soilId, HttpServletRequest request) {
    soilService.deleteSoilById(soilId, auth.doFilter(request, new Role.Builder().worker().organization().build()));
    return ResponseEntity.noContent().build();
  }

}
