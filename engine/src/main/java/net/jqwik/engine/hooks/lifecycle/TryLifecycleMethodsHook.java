package net.jqwik.engine.hooks.lifecycle;

import java.lang.reflect.*;
import java.util.*;

import org.junit.platform.engine.support.hierarchical.*;

import net.jqwik.api.lifecycle.*;
import net.jqwik.engine.hooks.*;
import net.jqwik.engine.support.*;

public class TryLifecycleMethodsHook implements AroundTryHook {

	private void beforeTry(TryLifecycleContext context) {
		List<Method> beforeTryMethods = LifecycleMethods.findBeforeTryMethods(context.propertyContext().containerClass());
		callPropertyMethods(beforeTryMethods, context.propertyContext().testInstance());
	}

	private void callPropertyMethods(List<Method> methods, Object testInstance) {
		ThrowableCollector throwableCollector = new ThrowableCollector(ignore -> false);
		for (Method method : methods) {
			throwableCollector.execute(() -> callMethod(method, testInstance));
		}
		throwableCollector.assertEmpty();
	}

	private void callMethod(Method method, Object target) {
		JqwikReflectionSupport.invokeMethodPotentiallyOuter(method, target);
	}

	private void afterTry(TryLifecycleContext context) {
		List<Method> afterTryMethods = LifecycleMethods.findAfterTryMethods(context.propertyContext().containerClass());
		callPropertyMethods(afterTryMethods, context.propertyContext().testInstance());
	}

	@Override
	public PropagationMode propagateTo() {
		return PropagationMode.ALL_DESCENDANTS;
	}

	@Override
	public boolean appliesTo(Optional<AnnotatedElement> element) {
		return element.map(e -> e instanceof Method).orElse(false);
	}

	@Override
	public int aroundTryProximity() {
		return Hooks.AroundTry.TRY_LIFECYCLE_METHODS_PROXIMITY;
	}

	@Override
	public TryExecutionResult aroundTry(TryLifecycleContext context, TryExecutor aTry, List<Object> parameters) {
		beforeTry(context);
		try {
			return aTry.execute(parameters);
		} finally {
			afterTry(context);
		}
	}
}
