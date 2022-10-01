package com.example.common.exception;

/**
 * 対象データが存在しない場合にスローされる例外。
 * 主にオンラインでハンドリングして404 Not Foundを返すために使用する。
 * 
 * @author sample
 * 
 */
public class DataNotFoundException extends RuntimeException {
}
