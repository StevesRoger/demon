package org.jarvis.ws.medicine.model.entity;

import org.jarvis.core.model.IdentityObject;
import org.jarvis.core.model.JsonSnakeCase;
import org.jarvis.core.model.base.AnyJson;

/**
 * Created: KimChheng
 * Date: 09-Nov-2020 Mon
 * Time: 2:34 PM
 */
public abstract class BaseEntity extends IdentityObject<Integer> implements AnyJson, JsonSnakeCase {
}
