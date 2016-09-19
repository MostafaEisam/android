package me.dong.simple_android_graph;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.inject.Scope;

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the activity to be memoized in the
 * correct component.
 *
 * 객체의 수명이 액티비티의 수명을 따를 경우 사용하는 사용자 정의 스코프 어노테이션
 * 장점
 *      1. 액티비가 생성되어있길 요구하는 부분에 객체 인텐트
 *      2. 액티비티 기반의 싱글톤 객체 사용
 *      3. 액티비티에서만 사용하는 것을 글로벌 객체 그래프와 분리
 */
@Scope
@Retention(RUNTIME)
public @interface PreActivity {
}
