package org.fourstack.business.model;

public record RuleConfiguration<T>(T object, ValidationRule config) {
}
