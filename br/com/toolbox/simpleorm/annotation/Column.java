package br.com.toolbox.simpleorm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

	int NONE = 0;
	int AUTO = 1;
	int SEQUENCE = 2;
	
	String name();
	boolean primaryKey() default false;
	int generator() default 0; 
}
