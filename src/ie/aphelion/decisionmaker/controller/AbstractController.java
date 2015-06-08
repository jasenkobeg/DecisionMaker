package ie.aphelion.decisionmaker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

/**
 * Common controller methods.
 * 
 * @author dude
 * @version 1.0 March 25, 2013
 */
abstract class AbstractController {

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);

	/** The name of the result page. */
	private static final String RESULT_PAGE_NAME = "result";

	/**
	 * Creates a model and view with the given object. 
	 * 
	 * @param The
	 *            model object to add.
	 * @return The model and View.
	 */
	ModelAndView createModelAndView(final Object model) {
		final ModelAndView modelAndView = new ModelAndView(RESULT_PAGE_NAME);
		modelAndView.addObject(model);
		LOG.debug("{}", modelAndView);
		return modelAndView;
	}

}
