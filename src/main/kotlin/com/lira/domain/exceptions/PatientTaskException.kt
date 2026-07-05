package com.lira.domain.exceptions

sealed class PatientTaskException(message: String) : RuntimeException(message) {
    class InvalidEntryTarget : PatientTaskException(
        "La entrada del diario debe referenciar exactamente un subobjetivo o una tarea propia"
    )
}
