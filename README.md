# 📖 Instrucciones de Uso - Colección Insomnia LP TP 2025

## ✅ Estado de tu Colección

Tu archivo **Insomnia_2025-11-21.yaml** ya está **100% configurado y listo para usar**.

---

## 🚀 Paso 1: Importar la Colección en Insomnia

### Opción A: Importar archivo YAML (RECOMENDADO)

1. **Abre Insomnia**
2. **Click en:** Application Menu (≡) → Import/Export → Import Data
3. **Selecciona:** From File
4. **Busca tu archivo:** `Insomnia_2025-11-21.yaml`
5. **Click en:** Import
6. ✅ **¡Listo!** La colección se cargará automáticamente

### Opción B: Copiar/Pegar (si tienes solo el contenido)

1. **Abre Insomnia**
2. **Click en:** Application Menu (≡) → Import/Export → Import Data
3. **Selecciona:** From Clipboard
4. **Pega el contenido YAML**
5. **Click en:** Import

---

## ⚙️ Paso 2: Configurar el Entorno

### Las variables ya están configuradas:

```yaml
base_url: http://localhost:8080
empleado_id: 1
gerente_id: 2
```

**Puedes cambiarlas según tus necesidades:**

1. **Click en el icono de engranaje (⚙️)** en la esquina superior derecha
2. **Selecciona:** Manage Environments
3. **Click en:** Local (tu entorno)
4. **Edita los valores:**
    - `base_url`: La URL base de tu aplicación (default: http://localhost:8080)
    - `empleado_id`: ID del empleado a probar (se actualiza automáticamente)
    - `gerente_id`: ID del gerente a probar (se actualiza automáticamente)
5. **Click en:** Done

---

## 📁 Paso 3: Estructura de Carpetas

Tu colección está organizada en **8 secciones principales**:

```
LP TP 2025 - Nómina y Permisos
│
├── 🔷 CREAR PERSONAS (Polimórfico Único)
│   ├── POST - Crear EmpleadoTiempoCompleto
│   ├── POST - Crear EmpleadoPorHoras
│   ├── POST - Crear Contratista
│   └── POST - Crear Gerente
│
├── 📋 GESTIONAR PERSONAS
│   ├── GET - Obtener Todas las Personas
│   ├── GET - Obtener Persona por ID
│   ├── GET - Buscar por Nombre
│   └── DELETE - Eliminar Persona
│
├── 📅 PERMISOS - EMPLEADOS (Límite 20 días/año)
│   ├── POST - Solicitar Vacaciones
│   ├── POST - Solicitar Permiso
│   └── GET - Consultar Días Disponibles
│
├── 👨‍💼 PERMISOS - GERENTES (Sin límite anual)
│   ├── POST - Solicitar Vacaciones (Máx 30 días consecutivos)
│   ├── POST - Solicitar Permiso (Máx 10 días)
│   └── GET - Consultar Días Disponibles
│
├── 💰 NÓMINA Y REPORTES (Objetivo 2)
│   ├── GET - Reporte Nómina Completo
│   ├── GET - Total Días Solicitados
│   ├── GET - Reporte Filtrado (>10 días)
│   └── GET - Reporte Filtrado (>20 días)
│
├── 📊 REMUNERACIONES
│   ├── GET - Listar Todos los Empleados (DTO)
│   ├── GET - Calcular Nómina Total (por tipo)
│   └── GET - Generar Reporte Completo (polimórfico)
│
├── 📦 BATCH PROCESSING
│   └── POST - Crear Personas en Batch
│
└── ⚠️ PRUEBA DE EXCEPCIONES
    ├── ERROR - EmpleadoNoEncontradoException (404)
    ├── ERROR - DiasInsuficientesException (400)
    ├── ERROR - PermisoDenegadoException (400) - Gerente >30 días
    ├── ERROR - ValidationException - Documento Vacío (400)
    └── ERROR - ValidationException - Fecha Nacimiento Futura (400)
```

---

## 🎯 Paso 4: Flujo de Prueba Recomendado

### **Opción A: Flujo Completo desde Cero**

#### 1. **Crear Empleados (Todos los tipos)**

```
🔷 CREAR PERSONAS
  → POST - Crear EmpleadoTiempoCompleto (obtener ID, ej: 1)
  → POST - Crear EmpleadoPorHoras (obtener ID, ej: 2)
  → POST - Crear Contratista (obtener ID, ej: 3)
  → POST - Crear Gerente (obtener ID, ej: 4)
```

**IMPORTANTE:** Copia los IDs que te devuelve cada respuesta para usarlos en los siguientes pasos.

#### 2. **Verificar Creación**

```
📋 GESTIONAR PERSONAS
  → GET - Obtener Todas las Personas (ver los 4 tipos creados)
  → GET - Obtener Persona por ID (probar con ID 1)
  → GET - Buscar por Nombre (ej: "Juan")
```

#### 3. **Probar Permisos - EmpleadoTiempoCompleto (Límite 20 días)**

```
📅 PERMISOS - EMPLEADOS
  → Actualizar {{ _.empleado_id }} con el ID del EmpleadoTiempoCompleto (ej: 1)
  → POST - Solicitar Vacaciones (10 días) ✅ DEBE FUNCIONAR
  → GET - Consultar Días Disponibles (ver: 10 vacaciones, 0 permisos)
  → POST - Solicitar Permiso (5 días) ✅ DEBE FUNCIONAR
  → GET - Consultar Días Disponibles (ver: 10 vacaciones, 5 permisos = 15/20 total)
  → POST - Solicitar Vacaciones (10 días más) ❌ DEBE DAR ERROR (excede 20)
```

#### 4. **Probar Permisos - EmpleadoPorHoras (Límite 20 días)**

```
📅 PERMISOS - EMPLEADOS
  → Actualizar {{ _.empleado_id }} con el ID del EmpleadoPorHoras (ej: 2)
  → POST - Solicitar Vacaciones (8 días) ✅ DEBE FUNCIONAR
  → GET - Consultar Días Disponibles
  → POST - Solicitar Permiso (7 días) ✅ DEBE FUNCIONAR
  → GET - Consultar Días Disponibles (ver: 15/20 total)
```

#### 5. **Probar Permisos - Contratista (Límite 20 días)**

```
📅 PERMISOS - EMPLEADOS
  → Actualizar {{ _.empleado_id }} con el ID del Contratista (ej: 3)
  → POST - Solicitar Vacaciones (12 días) ✅ DEBE FUNCIONAR
  → GET - Consultar Días Disponibles (ver: 12/20 total)
```

#### 6. **Probar Permisos - Gerente (SIN límite anual, pero límites por solicitud)**

```
👨‍💼 PERMISOS - GERENTES
  → Actualizar {{ _.gerente_id }} con el ID del Gerente (ej: 4)
  → POST - Solicitar Vacaciones (25 días) ✅ DEBE FUNCIONAR (gerente SÍ puede >20)
  → GET - Consultar Días Disponibles (ver: 25 vacaciones - sin límite anual)
  → POST - Solicitar Vacaciones (15 días más) ✅ DEBE FUNCIONAR (total: 40 días - sin límite)
  → POST - Solicitar Vacaciones (35 días) ❌ DEBE DAR ERROR (excede 30 consecutivos)
  → POST - Solicitar Permiso (8 días) ✅ DEBE FUNCIONAR
  → POST - Solicitar Permiso (12 días) ❌ DEBE DAR ERROR (excede 10 días)
```

#### 7. **Generar Reportes (Objetivo 2 del TP)**

```
💰 NÓMINA Y REPORTES
  → GET - Reporte Nómina Completo (ver todos los empleados con días solicitados)
  → GET - Total Días Solicitados (suma total de todos)
  → GET - Reporte Filtrado (>10 días) (filtrar empleados con más de 10 días)
  → GET - Reporte Filtrado (>20 días) (solo gerentes deberían aparecer)
```

#### 8. **Remuneraciones (Cálculos Polimórficos)**

```
📊 REMUNERACIONES
  → GET - Listar Todos los Empleados (DTO)
  → GET - Calcular Nómina Total (por tipo) (ver cálculo polimórfico)
  → GET - Generar Reporte Completo (reporte detallado)
```

#### 9. **Batch Processing (Crear múltiples empleados a la vez)**

```
📦 BATCH PROCESSING
  → POST - Crear Personas en Batch (crea 2 empleados en una sola request)
```

#### 10. **Probar Excepciones (GlobalExceptionHandler)**

```
⚠️ PRUEBA DE EXCEPCIONES
  → ERROR - EmpleadoNoEncontradoException (404) - ID inexistente
  → ERROR - DiasInsuficientesException (400) - Empleado excede 20 días
  → ERROR - PermisoDenegadoException (400) - Gerente >30 días consecutivos
  → ERROR - ValidationException - Documento Vacío (400)
  → ERROR - ValidationException - Fecha Nacimiento Futura (400)
```

---

### **Opción B: Flujo Rápido (2 minutos)**

Si solo quieres verificar que todo funciona:

```
1. 🔷 POST - Crear EmpleadoTiempoCompleto (copiar ID)
2. 🔷 POST - Crear Gerente (copiar ID)
3. 📋 GET - Obtener Todas las Personas
4. 📅 POST - Solicitar Vacaciones (empleado) ✅
5. 👨‍💼 POST - Solicitar Vacaciones (gerente, 25 días) ✅
6. 💰 GET - Reporte Nómina Completo
7. ⚠️ ERROR - DiasInsuficientesException (probar error)
```

---

## 🔄 Paso 5: Cómo Ejecutar cada Request

### Método 1: Click y Enviar

1. Click en la request que quieres ejecutar
2. El panel derecho mostrará los detalles
3. Click en el botón **"Send"** (esquina superior derecha)
4. Verás la respuesta en la pestaña **"Response"**

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

### Cambiar el empleado_id o gerente_id para cada request:

**Opción 1: En las Variables de Entorno**

1. Click en el icono de engranaje (⚙️)
2. Edita `empleado_id` con el ID del empleado que creaste
3. Edita `gerente_id` con el ID del gerente que creaste

**Opción 2: Directamente en la URL**

1. **Abre la request** que quieras modificar
2. **En la URL, busca:** `{{ _.empleado_id }}` o `{{ empleado_id }}`
3. **Reemplaza con:** El ID que quieras probar
4. **Ejemplo:**

```
Antes:  {{ base_url }}/empleados/{{ _.empleado_id }}/vacaciones
Después: http://localhost:8080/empleados/1/vacaciones
```

**Tip:** Usa `{{ _.empleado_id }}` para que Insomnia use el último ID creado automáticamente.

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
Cada request ya especifica su método (GET, POST, DELETE)
Puedes cambiar en el desplegable junto a la URL
```

### ✅ Formatear JSON

```
En "Response", el JSON se formatea automáticamente
Click en el icono de "Pretty" para mejor legibilidad
```

### ✅ Copiar IDs de Respuestas

```
Después de crear un empleado, copia su ID de la respuesta
Úsalo en las siguientes requests que requieran ese ID
```

---

## 🚨 Paso 8: Solución de Problemas

### Error: "Cannot GET /api/personas"

```
✓ Verificar que el servidor Spring Boot esté corriendo
✓ Verificar que base_url sea correcta (http://localhost:8080)
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
✓ Crear primero el empleado con POST /api/personas
✓ Usar el ID correcto devuelto en la respuesta
```

### Error: "DiasInsuficientesException" (Empleados regulares)

```
✓ Esto es ESPERADO cuando intentas superar 20 días anuales
✓ Es parte de la validación del sistema
✓ Los empleados regulares tienen límite de 20 días/año
✓ Crear un nuevo empleado si necesitas más pruebas
```

### Error: "PermisoDenegadoException" (Gerentes)

```
✓ Esto es ESPERADO cuando gerente intenta >30 días consecutivos
✓ O cuando intenta >10 días de permiso
✓ Los gerentes NO tienen límite anual (pueden >20 días/año)
✓ Pero SÍ tienen límites por solicitud individual
```

### Error: "Tipo de empleado no puede solicitar permisos"

```
✓ Verificar que el ID corresponda a un empleado Permisionable
✓ EmpleadoTiempoCompleto, EmpleadoPorHoras, Contratista y Gerente SÍ pueden
✓ Si creaste una Persona base, esta NO puede solicitar permisos
```

---

## 📊 Paso 9: Ejemplos de Respuestas

### ✅ Crear EmpleadoTiempoCompleto (201 Created)

```json
{
  "id": 1,
  "tipoEmpleado": "TIEMPO_COMPLETO",
  "nombre": "Juan",
  "apellido": "Pérez",
  "numeroDocumento": "12345678",
  "fechaNacimiento": "1990-05-15",
  "salarioMensual": 5000000,
  "departamento": "IT",
  "diasVacacionesAnuales": 0,
  "diasPermisoAnuales": 0
}
```

### ✅ Crear Gerente (201 Created)

```json
{
  "id": 2,
  "tipoEmpleado": "GERENTE",
  "nombre": "María",
  "apellido": "López",
  "numeroDocumento": "87654321",
  "fechaNacimiento": "1992-08-20",
  "salarioMensual": 8000000,
  "departamento": "Dirección",
  "bonoAnual": 0,
  "diasVacacionesAnuales": 0,
  "diasPermisoAnuales": 0
}
```

### ✅ Solicitar Vacaciones - Empleado (201 Created)

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
  "fechaFin": "2025-12-11",
  "motivo": "Descanso anual",
  "exitoso": true,
  "mensaje": "Vacaciones registradas correctamente para EmpleadoTiempoCompleto",
  "timestamp": "2025-11-21T10:30:00"
}
```

### ✅ Solicitar Vacaciones - Gerente 25 días (201 Created)

```json
{
  "empleadoId": 2,
  "nombreEmpleado": "María López",
  "tipoSolicitud": "VACACIONES_GERENTE",
  "diasSolicitados": 25,
  "diasVacacionesActuales": 25,
  "diasPermisoActuales": 0,
  "totalDiasSolicitados": 25,
  "fechaInicio": "2025-02-01",
  "fechaFin": "2025-02-26",
  "motivo": "Descanso directivo",
  "exitoso": true,
  "mensaje": "Vacaciones de gerente registradas correctamente.",
  "timestamp": "2025-11-21T10:35:00"
}
```

### ✅ Reporte Nómina Completo (200 OK)

```json
{
  "empleados": [
    {
      "id": 1,
      "nombre": "Juan Pérez",
      "tipo": "EmpleadoTiempoCompleto",
      "diasVacaciones": 10,
      "diasPermisos": 5,
      "totalDias": 15,
      "salario": 5000000
    },
    {
      "id": 2,
      "nombre": "María López",
      "tipo": "Gerente",
      "diasVacaciones": 25,
      "diasPermisos": 8,
      "totalDias": 33,
      "salario": 8000000
    }
  ],
  "totalDiasSolicitados": 48,
  "timestamp": "2025-11-21T10:40:00"
}
```

### ❌ Error DiasInsuficientesException (400 Bad Request)

```json
{
  "message": "Vacaciones rechazadas: supera el límite anual de 20 días. Ya tiene 15 días solicitados y quiere agregar 10 más.",
  "status": 400
}
```

### ❌ Error PermisoDenegadoException - Gerente (400 Bad Request)

```json
{
  "message": "Vacaciones rechazadas para gerente: excede el límite de 30 días consecutivos",
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

### ❌ Error ValidationException (400 Bad Request)

```json
{
  "status": 400,
  "error": "BAD_REQUEST",
  "timestamp": "2025-11-21T10:45:00",
  "validationErrors": {
    "numeroDocumento": "El número de documento no puede estar vacío."
  }
}
```

---


---

## 📞 Referencia Rápida: URLs Principales

| Categoría | Método | Endpoint | Descripción |
|-----------|--------|----------|-------------|
| **Crear** | POST | `/api/personas` | Crear cualquier tipo de empleado (polimórfico) |
| **Obtener** | GET | `/api/personas` | Listar todas las personas |
| **Obtener** | GET | `/api/personas/{id}` | Obtener persona por ID |
| **Buscar** | GET | `/api/personas?nombre=X` | Buscar por nombre |
| **Eliminar** | DELETE | `/api/personas/{id}` | Eliminar persona |
| **Vacaciones (Empleado)** | POST | `/empleados/{id}/vacaciones` | Solicitar vacaciones (límite 20 días/año) |
| **Permisos (Empleado)** | POST | `/empleados/{id}/permisos` | Solicitar permiso (límite 20 días/año total) |
| **Consultar (Empleado)** | GET | `/empleados/{id}/dias-disponibles` | Ver días disponibles |
| **Vacaciones (Gerente)** | POST | `/gerentes/{id}/vacaciones` | Solicitar vacaciones (sin límite anual, máx 30 consecutivos) |
| **Permisos (Gerente)** | POST | `/gerentes/{id}/permisos` | Solicitar permiso (sin límite anual, máx 10 por solicitud) |
| **Consultar (Gerente)** | GET | `/gerentes/{id}/dias-disponibles` | Ver días solicitados |
| **Reporte Completo** | GET | `/nomina/reporte` | Reporte JSON completo de nómina |
| **Reporte Filtrado** | GET | `/nomina/reporte/filtrado?dias=N` | Empleados con más de N días |
| **Total Días** | GET | `/nomina/total-dias` | Suma total de días solicitados |
| **Batch** | POST | `/api/personas/batch` | Crear múltiples empleados |

---

## 🔑 Conceptos Clave del Sistema

### Límites de Días por Tipo de Empleado

| Tipo de Empleado | Límite Anual | Límite por Solicitud | Observaciones |
|------------------|--------------|----------------------|---------------|
| **EmpleadoTiempoCompleto** | 20 días/año | Sin límite por solicitud | Vacaciones + Permisos ≤ 20 |
| **EmpleadoPorHoras** | 20 días/año | Sin límite por solicitud | Vacaciones + Permisos ≤ 20 |
| **Contratista** | 20 días/año | Sin límite por solicitud | Vacaciones + Permisos ≤ 20 |
| **Gerente** | **Sin límite anual** | Vacaciones: máx 30 días consecutivos<br>Permisos: máx 10 días | Puede superar 20 días/año |

### Excepciones del Sistema

| Excepción | Tipo | Status HTTP | Cuándo se lanza |
|-----------|------|-------------|-----------------|
| `DiasInsuficientesException` | Checked | 400 | Empleado regular intenta superar 20 días/año |
| `PermisoDenegadoException` | Checked | 400 | Gerente supera límites por solicitud |
| `EmpleadoNoEncontradoException` | Runtime | 404 | ID no existe en BD |
| `ValidationException` | Spring | 400 | Datos inválidos en request |

---

## 📚 Documentación Adicional

Para más información sobre el diseño del sistema, consulta:

- `README.md` del proyecto
- Documentación de la API REST
- Diagramas de clases
- Casos de uso del trabajo práctico

