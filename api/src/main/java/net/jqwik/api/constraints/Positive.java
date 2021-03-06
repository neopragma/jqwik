package net.jqwik.api.constraints;

import java.lang.annotation.*;

import org.apiguardian.api.*;

import static org.apiguardian.api.API.Status.*;

/**
 * Constrain the range of a generated number to be greater than 0.
 *
 * Applies to numeric parameters which are also annotated with {@code @ForAll}.
 *
 * @see net.jqwik.api.ForAll
 * @see Negative
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = MAINTAINED, since = "1.0")
public @interface Positive {
}
