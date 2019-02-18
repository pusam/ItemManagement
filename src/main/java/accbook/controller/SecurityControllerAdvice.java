package accbook.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(value = "accbook")
public class SecurityControllerAdvice
{
    @ExceptionHandler(Exception.class)
    public ModelAndView exception(Exception e)
    {
        ModelAndView mav = new ModelAndView("exception");
        mav.addObject("name", e.getClass().getSimpleName());
        mav.addObject("message", e.getMessage());
        mav.addObject("test", "왜냐고");

        return mav;

    }

}