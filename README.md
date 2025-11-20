# 📖 Instrucciones de Uso - Colección Insomnia LP TP 2025

## ✅ Estado de tu Colección

Tu archivo `Insomnia_2025-11-19.yaml` ya está **100% configurado y listo para usar**.

---

## 🚀 Paso 1: Importar la Colección en Insomnia

### Opción A: Importar archivo YAML (RECOMENDADO)

1. **Abre Insomnia**
2. **Click en:** `Application Menu (≡)` → `Import/Export` → `Import Data`
3. **Selecciona:** `From File`
4. **Busca tu archivo:** `Insomnia_2025-11-19.yaml`
5. **Click en:** `Import`
6. ✅ **¡Listo!** La colección se cargará automáticamente

### Opción B: Copiar/Pegar (si tienes solo el contenido)

1. **Abre Insomnia**
2. **Click en:** `Application Menu (≡)` → `Import/Export` → `Import Data`
3. **Selecciona:** `From Clipboard`
4. **Pega el contenido YAML**
5. **Click en:** `Import`

---

## ⚙️ Paso 2: Configurar el Entorno

### Las variables ya están configuradas:

```
base_url: http://localhost:8080
empleado_id: 1
gerente_id: 2
```

**Pero puedes cambiarlas según tus necesidades:**

1. **Click en el icono de engranaje (⚙️)** en la esquina superior derecha
2. **Selecciona:** `Manage Environments`
3. **Click en:** `Local` (tu entorno)
4. **Edita los valores:**
    - `base_url`: La URL base de tu aplicación (default: `http://localhost:8080`)
    - `empleado_id`: ID del empleado a probar (default: `1`)
    - `gerente_id`: ID del gerente a probar (default: `2`)
5. **Click en:** `Done`

---

## 📁 Paso 3: Estructura de Carpetas

Tu colección está organizada en **8 secciones principales**:

```
LP TP 2025 - Nómina y Permisos
├── 🔷 CREAR PERSONAS (Polimórfico Único)
│   ├── POST - Crear EmpleadoTiempoCompleto
│   ├── POST - Crear Gerente
│   ├── POST - Crear EmpleadoPorHoras
│   ├── POST - Crear Contratista
│   
│
├── 📋 GESTIONAR PERSONAS
│   ├── GET - Obtener Todas las Personas
│   ├── GET - Obtener Persona por ID
│   ├── GET - Buscar por Nombre
│   ├── PUT - Actualizar Persona
│   └── DELETE - Eliminar Persona
│
├── 💼 CÁLCULOS ESPECIALIZADOS
│   └── GET - Calcular Impuestos Detallado
│
├── 📅 PERMISOS - EMPLEADOS (Límite 20 días/año)
│   ├── POST - Solicitar Vacaciones
│   ├── POST - Solicitar Permiso
│   └── GET - Consultar Días Disponibles
│
├── 👨‍💼 PERMISOS - GERENTES (Sin límite anual)
│   ├── POST - Solicitar Vacaciones (Máx 30 días)
│   ├── POST - Solicitar Permiso (Máx 10 días)
│   └── GET - Consultar Días Disponibles
│
├── 💰 NÓMINA Y REPORTES (Objetivo 2)
│   ├── GET - Reporte Nómina Completo
│   ├── GET - Reporte Filtrado (>10 días)
│   ├── GET - Reporte Filtrado (>20 días)
│   └── GET - Total Días Solicitados
│
├── 📊 REMUNERACIONES
│   ├── GET - Listar Todos los Empleados
│   ├── GET - Calcular Nómina Total
│   └── GET - Generar Reporte Completo
│
├── 📦 BATCH PROCESSING
│   └── POST - Crear Personas en Batch
│
└── ⚠️ PRUEBA DE EXCEPCIONES
    ├── ERROR - EmpleadoNoEncontradoException (404)
    ├── ERROR - DiasInsuficientesException (400)
    ├── ERROR - ValidationException - Documento Vacío (400)
    ├── ERROR - ValidationException - Fecha Nacimiento Futura (400)
    └── ERROR - PermisoDenegadoException (400)
```

---

## 🎯 Paso 4: Flujo de Prueba Recomendado

### **Opción A: Flujo Completo desde Cero**

#### 1. **Crear Empleados**
```
🔷 CREAR PERSONAS
  → POST - Crear EmpleadoTiempoCompleto (obtener ID 1)
  → POST - Crear Gerente (obtener ID 2)
  → POST - Crear EmpleadoPorHoras
  → POST - Crear Contratista
```

#### 2. **Verificar Creación**
```
📋 GESTIONAR PERSONAS
  → GET - Obtener Todas las Personas
  → GET - Obtener Persona por ID (usar ID 1)
```

#### 3. **Probar Permisos - Empleado Común**
```
📅 PERMISOS - EMPLEADOS
  → POST - Solicitar Vacaciones (10 días) ✅
  → GET - Consultar Días Disponibles (ver: 10/20 usados)
  → POST - Solicitar Vacaciones (15 días más) ❌ DEBE DAR ERROR
```

#### 4. **Probar Permisos - Gerente (sin límite anual)**
```
👨‍💼 PERMISOS - GERENTES
  → POST - Solicitar Vacaciones (25 días) ✅ (gerente SÍ puede >20)
  → GET - Consultar Días Disponibles
```

#### 5. **Generar Reportes**
```
💰 NÓMINA Y REPORTES
  → GET - Reporte Nómina Completo
  → GET - Reporte Filtrado (>10 días)
  → GET - Total Días Solicitados
```

#### 6. **Probar Excepciones**
```
⚠️ PRUEBA DE EXCEPCIONES
  → Ejecutar cada uno para verificar GlobalExceptionHandler
```

---

### **Opción B: Flujo Rápido (30 segundos)**

Si solo quieres verificar que todo funciona:

```
1. 🔷 POST - Crear EmpleadoTiempoCompleto
2. 📋 GET - Obtener Todas las Personas
3. 💰 GET - Reporte Nómina Completo
4. ⚠️ ERROR - EmpleadoNoEncontradoException (probar error)
```

---

## 🔄 Paso 5: Cómo Ejecutar cada Request

### Método 1: Click y Enviar
```
1. Click en la request que quieres ejecutar
2. El panel derecho mostrará los detalles
3. Click en el botón "Send" (esquina superior derecha)
4. Verás la respuesta en la pestaña "Response"
```

### Método 2: Atajo de Teclado
```
Ctrl + Enter (Windows/Linux)
Cmd + Enter (Mac)
```

### Método 3: Desde el Árbol
```
Click derecho en la request → Send Request
```

---

## 📝 Paso 6: Modificar Variables en Requests

### Cambiar el empleado_id para cada request:

1. **Abre la request** que quieras modificar
2. **En la URL, busca:** `{{ empleado_id }}`
3. **Reemplaza con:** El ID que quieras probar
4. **Ejemplo:**
   ```
   Antes: {{ base_url }}/empleados/{{ empleado_id }}/vacaciones
   Después: http://localhost:8080/empleados/1/vacaciones
   ```

**Tip:** Usa `{{ empleado_id }}` para que Insomnia reemplace automáticamente con el valor del entorno.

---

## 💡 Paso 7: Consejos Prácticos

### ✅ Guardar Respuestas
```
En la pestaña "Response"
Click en: "Save Response" → Guardar como archivo
```

### ✅ Ver Historial de Requests
```
Click en: "Timeline" (abajo)
Ver todas las requests ejecutadas en orden
```

### ✅ Examinar Headers
```
En "Response" → Pestaña "Headers"
Ver headers de respuesta (Content-Type, Status, etc)
```

### ✅ Probar Diferentes Métodos HTTP
```
Cada request ya especifica su método (GET, POST, PUT, DELETE)
Puedes cambiar en el desplegable junto a la URL
```

### ✅ Formatear JSON
```
En "Response", el JSON se formatea automáticamente
Click en el icono de "Pretty" para mejor legibilidad
```

---

## 🚨 Paso 8: Solución de Problemas

### Error: "Cannot GET /api/personas"
```
✓ Verificar que el servidor Spring Boot esté corriendo
✓ Verificar la variable base_url es correcta
✓ Verificar puerto (default: 8080)
```

### Error: "Connection refused"
```
✓ Iniciar la aplicación: mvn spring-boot:run
✓ O ejecutar desde IDE (Spring Boot Run)
✓ Esperar a que esté lista (ver "Started..." en logs)
```

### Error: "Validation failed"
```
✓ Verificar que los campos requeridos estén completos
✓ Verificar que los tipos de datos sean correctos
✓ Ver pestaña "Response" para detalles del error
```

### Error: "EmpleadoNoEncontradoException"
```
✓ El ID no existe en la base de datos
✓ Crear primero el empleado
✓ Usar un ID válido
```

### Error: "DiasInsuficientesException"
```
✓ Esto es ESPERADO en algunos tests
✓ El empleado ya usó todos sus días
✓ Crear un nuevo empleado o verificar los días disponibles
```

---

## 📊 Paso 9: Ejemplos de Respuestas

### ✅ Crear Empleado (201 Created)
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "numeroDocumento": "12345678",
  "fechaNacimiento": "1990-05-15",
  "salarioMensual": 5000000,
  "departamento": "IT"
}
```

### ✅ Solicitar Vacaciones (201 Created)
```json
{
  "empleadoId": 1,
  "nombreEmpleado": "Juan Pérez",
  "tipoSolicitud": "VACACIONES",
  "diasSolicitados": 10,
  "diasVacacionesActuales": 10,
  "diasPermisoActuales": 0,
  "totalDiasSolicitados": 10,
  "fechaInicio": "2025-12-01",
  "fechaFin": "2025-12-10",
  "motivo": "Descanso anual",
  "exitoso": true,
  "mensaje": "Vacaciones registradas correctamente.",
  "timestamp": "2025-11-19T10:30:00"
}
```

### ❌ Error DiasInsuficientesException (400 Bad Request)
```json
{
  "message": "Vacaciones rechazadas: supera el límite anual de 20 días...",
  "status": 400
}
```

### ❌ Error EmpleadoNoEncontradoException (404 Not Found)
```json
{
  "message": "Empleado no encontrado con ID: 99999",
  "status": 404
}
```

---

## 📋 Paso 10: Checklist Final

Antes de entregar tu trabajo, verifica:

```
✅ [ ] Insomnia importada correctamente
✅ [ ] Entorno "Local" configurado
✅ [ ] Base URL correcta (http://localhost:8080)
✅ [ ] Crear EmpleadoTiempoCompleto funciona
✅ [ ] Crear Gerente funciona
✅ [ ] Solicitar vacaciones (empleado) funciona
✅ [ ] Solicitar vacaciones (gerente) funciona
✅ [ ] Gerente PUEDE solicitar >20 días/año
✅ [ ] Empleado NO PUEDE solicitar >20 días/año
✅ [ ] Reporte nómina genera JSON
✅ [ ] Reporte filtrado por días funciona
✅ [ ] GlobalExceptionHandler captura errores correctamente
✅ [ ] 30 endpoints probados y funcionando
```

---

## 🎉 ¡Listo!

Tu colección Insomnia está **completamente configurada y lista para usar**.

**Próximos pasos:**
1. Abre Insomnia
2. Importa el archivo YAML
3. Comienza a probar los endpoints
4. Verifica todos los casos de uso
5. ¡Entrega con confianza! 🚀

---

## 📞 Recordatorio: URLs de Referencia

| Categoría | Endpoint |
|-----------|----------|
| **Crear** | `POST /api/personas` |
| **Obtener** | `GET /api/personas` o `/api/personas/{id}` |
| **Actualizar** | `PUT /api/personas/{id}` |
| **Eliminar** | `DELETE /api/personas/{id}` |
| **Vacaciones (Empleado)** | `POST /empleados/{id}/vacaciones` |
| **Vacaciones (Gerente)** | `POST /gerentes/{id}/vacaciones` |
| **Permisos (Empleado)** | `POST /empleados/{id}/permisos` |
| **Permisos (Gerente)** | `POST /gerentes/{id}/permisos` |
| **Reportes** | `GET /nomina/reporte` o `/nomina/reporte/filtrado?dias=N` |
| **Impuestos** | `GET /api/empleados-tiempo-completo/{id}/impuesto` |