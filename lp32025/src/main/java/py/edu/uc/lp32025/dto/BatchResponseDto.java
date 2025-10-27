package py.edu.uc.lp32025.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BatchResponseDto {

    public static class ItemResult {
        private Long id;
        private String nombre;
        private boolean success;
        private String message;

        public ItemResult() {}

        public ItemResult(Long id, String nombre, boolean success, String message) {
            this.id = id;
            this.nombre = nombre;
            this.success = success;
            this.message = message;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    private LocalDateTime timestamp = LocalDateTime.now();
    private List<ItemResult> results = new ArrayList<>();

    public BatchResponseDto() {}

    public LocalDateTime getTimestamp() { return timestamp; }
    public List<ItemResult> getResults() { return results; }
    public void setResults(List<ItemResult> results) { this.results = results; }

    public void addResult(ItemResult r) { this.results.add(r); }
}
