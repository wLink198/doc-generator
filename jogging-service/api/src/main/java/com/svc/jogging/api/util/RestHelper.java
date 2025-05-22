package com.svc.jogging.api.util;

import com.svc.jogging.model.exception.BusinessException;
import com.svc.jogging.model.res.ResponseData;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.util.stream.Collectors;

public class RestHelper {

	public static <T> ResponseEntity<T> createResponse(ResponseData res) {
		if (res.isOk()) {
			return ResponseEntity.status(HttpStatus.OK).body((T) res);
		}

		if (res.getException() instanceof BusinessException
				|| res.getException() instanceof MissingRequestHeaderException
				|| res.getException() instanceof HttpMediaTypeNotSupportedException) {
			res.setError(new ResponseData.Error(HttpStatus.BAD_REQUEST.value(), res.getException().getMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) res);
		}

		if (res.getException() instanceof MethodArgumentNotValidException) {
			String msg = ((MethodArgumentNotValidException) res.getException()).getBindingResult().getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
			res.setError(new ResponseData.Error(HttpStatus.BAD_REQUEST.value(), msg));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((T) res);
		}

		res.setError(new ResponseData.Error("The system encountered an unknown error. Contact admin for more information."));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((T) res);
	}
}
