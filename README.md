### Visión general del proyecto ###

El proyecto **LabRat StudyCards** es una aplicación Android desarrollada en Kotlin siguiendo una arquitectura basada en:

- **MVVM (Model - View - ViewModel)**
- **ROOM Database** para persistencia local
- **RecyclerView** para listas dinámicas
- **Coroutines** para tareas asíncronas
- **ViewBinding** para conectar XML con Kotlin

La aplicación está enfocada en:

- Crear tarjetas de estudio
- Organizar contenido en capítulos y mazos
- Estudiar usando repetición espaciada
- Gestionar estadísticas y progreso
- Configuración y perfiles de usuario

### Arquitectura real del proyecto ###
La arquitectura completa se ve así:

  UI (Activities + Adapters) → ViewModel → Repository → DAO → ROOM Database

### Flujo típico dentro de la app ###
## Crear una tarjeta

  addCardActivity → addCardViewModel → studyCardRepository → studyCardDao → ROOM Database

## Estudiar una tarjeta

  studyActivity → studyViewModel → spacedRepAlg → Actualiza estadístiscas
