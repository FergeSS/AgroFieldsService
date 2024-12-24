package agroscience.fields.controllers;

import agroscience.fields.dto.crop.RequestCrop;
import agroscience.fields.dto.crop.ResponseCrop;
import agroscience.fields.mappers.CropMapper;
import agroscience.fields.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/admin")
public class AdminController {

  private final AdminService cropsService;
  private final CropMapper cropMapper;

  @PostMapping
  @Operation(description = "Создание культуры")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {@Content(schema = @Schema())})
  })
  public ResponseCrop createCrop(@Valid @RequestBody RequestCrop request) {
    return cropMapper.map(cropsService.createCrop(cropMapper.map(request)));
  }

  @PutMapping
  @Operation(description = "Обновление культуры")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = {@Content(schema = @Schema())})
  })
  public ResponseCrop updateCrop(@Valid @Min(1) Long cropId, @Valid @RequestBody RequestCrop request) {
    return cropMapper.map(cropsService.updateCrop(cropId, cropMapper.map(request)));
  }

  @DeleteMapping
  @Operation(description = "Удаление культуры, вместе с ней удалятся и все севообороты, которые на неё ссылаются")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseEntity<Void> deleteCrop(@Valid @Min(1) Long cropId) {
    cropsService.deleteCropById(cropId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(description = "Получение культуры по id")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Удачно"),
    @ApiResponse(responseCode = "404", description = "Контент не найден", content = {@Content(schema = @Schema())}),
    @ApiResponse(responseCode = "500", description = "Неизвестная ошибка", content = {@Content(schema = @Schema())})
  })
  public ResponseCrop getCrop(@Valid @Min(1) Long cropId) {
    return cropMapper.map(cropsService.getCrop(cropId));
  }

}
