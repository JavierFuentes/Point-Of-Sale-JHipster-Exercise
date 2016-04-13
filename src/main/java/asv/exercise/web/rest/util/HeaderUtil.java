package asv.exercise.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 *
 */
public class HeaderUtil {

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-pOSDesignPatternsExerciseApp-alert", message);
        headers.add("X-pOSDesignPatternsExerciseApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("pOSDesignPatternsExerciseApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("pOSDesignPatternsExerciseApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("pOSDesignPatternsExerciseApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-pOSDesignPatternsExerciseApp-error", "error." + errorKey);
        headers.add("X-pOSDesignPatternsExerciseApp-params", entityName);
        return headers;
    }
}
