package org.jarvis.ws.medicine.component;

import org.jarvis.core.component.AbstractResponseBodyAdvice;
import org.jarvis.ws.medicine.controller.MedicineController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created: KimChheng
 * Date: 17-Jun-2020 Wed
 * Time: 22:05
 */
@RestControllerAdvice(basePackageClasses = {MedicineController.class})
public class JarvisResponseBodyAdvice extends AbstractResponseBodyAdvice {

}
