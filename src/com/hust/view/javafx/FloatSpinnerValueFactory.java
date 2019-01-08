package com.hust.view.javafx;

import javafx.beans.NamedArg;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.FloatStringConverter;

public class FloatSpinnerValueFactory extends SpinnerValueFactory<Float> {

	/**
	 * ********************************************************************* *
	 * Constructors * *
	 * ********************************************************************
	 */
	/**
	 * Constructs a new FloatSpinnerValueFactory that sets the initial value to be
	 * equal to the min value, and a default {@code amountToStepBy} of one.
	 *
	 * @param min The minimum allowed float value for the Spinner.
	 * @param max The maximum allowed float value for the Spinner.
	 */
	public FloatSpinnerValueFactory(@NamedArg("min") float min, @NamedArg("max") float max) {
		this(min, max, min);
	}

	/**
	 * Constructs a new FloatSpinnerValueFactory with a default
	 * {@code amountToStepBy} of one.
	 *
	 * @param min          The minimum allowed float value for the Spinner.
	 * @param max          The maximum allowed float value for the Spinner.
	 * @param initialValue The value of the Spinner when first instantiated, must be
	 *                     within the bounds of the min and max arguments, or else
	 *                     the min value will be used.
	 */
	public FloatSpinnerValueFactory(@NamedArg("min") float min, @NamedArg("max") float max,
			@NamedArg("initialValue") float initialValue) {
		this(min, max, initialValue, 1);
	}

	/**
	 * Constructs a new IntegerSpinnerValueFactory.
	 *
	 * @param min            The minimum allowed float value for the Spinner.
	 * @param max            The maximum allowed float value for the Spinner.
	 * @param initialValue   The value of the Spinner when first instantiated, must
	 *                       be within the bounds of the min and max arguments, or
	 *                       else the min value will be used.
	 * @param amountToStepBy The amount to increment or decrement by, per step.
	 */
	public FloatSpinnerValueFactory(@NamedArg("min") float min, @NamedArg("max") float max,
			@NamedArg("initialValue") float initialValue, @NamedArg("amountToStepBy") float amountToStepBy) {
		setMin(min);
		setMax(max);
		setAmountToStepBy(amountToStepBy);
		setConverter(new FloatStringConverter());

		valueProperty().addListener((o, oldValue, newValue) -> {
			// when the value is set, we need to react to ensure it is a
			// valid value (and if not, blow up appropriately)
			if (newValue < getMin()) {
				setValue(getMin());
			} else if (newValue > getMax()) {
				setValue(getMax());
			}
		});
		setValue(initialValue >= min && initialValue <= max ? initialValue : min);
	}

	/**
	 * ********************************************************************* *
	 * Properties * *
	 * ********************************************************************
	 */
	// --- min
	private FloatProperty min = new SimpleFloatProperty(this, "min") {
		@Override
		protected void invalidated() {
			Float currentValue = FloatSpinnerValueFactory.this.getValue();
			if (currentValue == null) {
				return;
			}

			float newMin = get();
			if (newMin > getMax()) {
				setMin(getMax());
				return;
			}

			if (currentValue < newMin) {
				FloatSpinnerValueFactory.this.setValue(newMin);
			}
		}
	};

	public final void setMin(float value) {
		min.set(value);
	}

	public final float getMin() {
		return min.get();
	}

	/**
	 * Sets the minimum allowable value for this value factory
	 */
	public final FloatProperty minProperty() {
		return min;
	}

	// --- max
	private FloatProperty max = new SimpleFloatProperty(this, "max") {
		@Override
		protected void invalidated() {
			Float currentValue = FloatSpinnerValueFactory.this.getValue();
			if (currentValue == null) {
				return;
			}

			float newMax = get();
			if (newMax < getMin()) {
				setMax(getMin());
				return;
			}

			if (currentValue > newMax) {
				FloatSpinnerValueFactory.this.setValue(newMax);
			}
		}
	};

	public final void setMax(float value) {
		max.set(value);
	}

	public final float getMax() {
		return max.get();
	}

	/**
	 * Sets the maximum allowable value for this value factory
	 */
	public final FloatProperty maxProperty() {
		return max;
	}

	// --- amountToStepBy
	private FloatProperty amountToStepBy = new SimpleFloatProperty(this, "amountToStepBy");

	public final void setAmountToStepBy(float value) {
		amountToStepBy.set(value);
	}

	public final float getAmountToStepBy() {
		return amountToStepBy.get();
	}

	/**
	 * Sets the amount to increment or decrement by, per step.
	 */
	public final FloatProperty amountToStepByProperty() {
		return amountToStepBy;
	}

	/**
	 * ********************************************************************* *
	 * Overridden methods * *
	 * ********************************************************************
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decrement(int steps) {
		final float min = getMin();
		final float newIndex = getValue() - steps * getAmountToStepBy();
		setValue(newIndex >= min ? newIndex : min);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increment(int steps) {
		final float max = getMax();
		final float currentValue = getValue();
		final float newIndex = currentValue + steps * getAmountToStepBy();
		setValue(newIndex <= max ? newIndex : max);
	}
}
