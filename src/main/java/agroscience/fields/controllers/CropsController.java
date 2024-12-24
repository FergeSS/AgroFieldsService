package agroscience.fields.controllers;

import agroscience.fields.dto.crop.RequestGetCrops;
import agroscience.fields.dto.crop.ResponseCrop;
import agroscience.fields.mappers.CropMapper;
import agroscience.fields.security.AuthoriseService;
import agroscience.fields.security.Role;
import agroscience.fields.services.CropsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/fields")
public class CropsController {

  private final CropsService cropsService;
  private final CropMapper cropMapper;
  private final AuthoriseService auth;

  @GetMapping("/crops")
  @Operation(description = "Get all crops by name with pagination")
  public List<ResponseCrop> getCrops(RequestGetCrops request, HttpServletRequest header) {
    auth.doFilter(header, new Role.Builder().worker().organization().build());
    return cropsService.getCrop(request.getName(), request.getPage(), request.getSize())
            .stream().map(cropMapper::map).toList();
  }

}
