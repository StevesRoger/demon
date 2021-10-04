package org.jarvis.ws.medicine.model.request;

import org.jarvis.core.model.IdentityObject;
import org.jarvis.core.model.base.AnyJson;
import org.jarvis.ws.medicine.model.UserContext;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 10:21 AM
 */
public abstract class BaseRequest extends IdentityObject<Integer> implements AnyJson, UserContext {
}
