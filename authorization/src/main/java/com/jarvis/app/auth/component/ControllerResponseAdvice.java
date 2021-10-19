package com.jarvis.app.auth.component;

import com.jarvis.app.auth.controller.UserController;
import com.jarvis.frmk.core.component.AbstractResponseBodyAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created: KimChheng
 * Date: 17-Jun-2020 Wed
 * Time: 22:05
 */
@RestControllerAdvice(basePackageClasses = {UserController.class})
public class ControllerResponseAdvice extends AbstractResponseBodyAdvice {
}
