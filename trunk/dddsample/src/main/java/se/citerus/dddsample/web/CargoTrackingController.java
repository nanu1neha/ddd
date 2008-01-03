package se.citerus.dddsample.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import se.citerus.dddsample.domain.Cargo;
import se.citerus.dddsample.service.CargoService;
import se.citerus.dddsample.web.command.TrackCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for tracking cargo.
 */
public class CargoTrackingController extends SimpleFormController {

  /**
   * Service instance.
   */
  private CargoService cargoService;

  public CargoTrackingController() {
    setCommandClass(TrackCommand.class);
  }

  @Override
  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
    final TrackCommand trackCommand = (TrackCommand) command;
    final Cargo cargo = cargoService.find(trackCommand.getTrackingId());

    final Map<String, Cargo> model = new HashMap<String, Cargo>();
    if (cargo != null) {
      model.put("cargo", cargo);
    } else {
      errors.rejectValue("trackingId", "cargo.unknown_id", new Object[] {trackCommand.getTrackingId()}, "Unknown cargo id");
    }
    return showForm(request, response, errors, model);
  }
   

  /**
   * Sets the cargo service instance.
   *
   * @param cargoService The service.
   */
  public void setCargoService(final CargoService cargoService) {
    this.cargoService = cargoService;
  }
  
}