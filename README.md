#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#define NUM_FILOSOFOS 5
#define LIMITE_COMIDAS 5 // Cada filósofo comerá exactamente 5 veces

// Arreglo de semáforos para los palillos (1 = libre, 0 = ocupado)
sem_t palillos[NUM_FILOSOFOS];

// Arreglo global para contar las ejecuciones (comidas) de cada hilo
int contador_comidas[NUM_FILOSOFOS] = {0, 0, 0, 0, 0};

// Función que ejecuta cada hilo (filósofo)
void* filosofo(void* num) {
    int id = *(int*)num;

    // Cambiamos el while(1) por un bucle condicionado al límite de ejecuciones
    while (contador_comidas[id] < LIMITE_COMIDAS) {
        
        printf("[Filósofo %d] PENSANDO... (Comidas: %d/%d)\n", id, contador_comidas[id], LIMITE_COMIDAS);
        usleep(rand() % 500000); // Piensa por un tiempo aleatorio

        printf("[Filósofo %d] Tienes HAMBRE...\n", id);

        // --- SOLUCIÓN PARA EVITAR DEADLOCK (Inversión de Recursos) ---
        if (id == NUM_FILOSOFOS - 1) {
            // El último filósofo (4) toma primero el derecho y luego el izquierdo
            sem_wait(&palillos[(id + 1) % NUM_FILOSOFOS]);
            sem_wait(&palillos[id]);
        } else {
            // Los filósofos 0, 1, 2 y 3 toman primero el izquierdo y luego el derecho
            sem_wait(&palillos[id]);
            sem_wait(&palillos[(id + 1) % NUM_FILOSOFOS]);
        }

        // --- REGIÓN CRÍTICA: COMER ---
        printf("[Filósofo %d] está COMIENDO...\n", id);
        usleep(rand() % 500000); // Come por un tiempo aleatorio
        
        // Incrementamos el contador individual de este hilo
        contador_comidas[id]++;

        // --- SALIDA DE LA REGIÓN CRÍTICA: SOLTAR PALILLOS ---
        printf("[Filósofo %d] Terminó de comer y libera los palillos. 🚪\n", id);
        sem_post(&palillos[id]);
        sem_post(&palillos[(id + 1) % NUM_FILOSOFOS]);
        
        usleep(100000); // Breve pausa de sincronización
    }
    
    printf("✅ [Filósofo %d] Ha completado sus %d comidas. Saliendo del hilo...\n", id, LIMITE_COMIDAS);
    return NULL;
}

int main() {
    pthread_t hilos[NUM_FILOSOFOS];
    int id_filosofos[NUM_FILOSOFOS];

    printf("===============================================================\n");
    printf("[SISTEMA] Inicializando Simulación de Filósofos Comelones...\n");
    printf("[SISTEMA] Meta: Cada filósofo debe ejecutar %d comidas de forma segura.\n", LIMITE_COMIDAS);
    printf("===============================================================\n\n");

    // 1. Inicializar los semáforos (palillos en 1 = libres)
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        sem_init(&palillos[i], 0, 1);
    }

    // 2. Crear los hilos secundarios (los filósofos)
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        id_filosofos[i] = i;
        pthread_create(&hilos[i], NULL, filosofo, &id_filosofos[i]);
    }

    // 3. Sincronización: Esperar a que TODOS los hilos terminen sus 5 ciclos por igual
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        pthread_join(hilos[i], NULL);
    }

    // 4. --- MOSTRADOR DE EJECUCIÓN FINAL (MÉTRICAS DEL SISTEMA OPERATIVO) ---
    printf("\n===============================================================\n");
    printf("        REPORTE FINAL DE EJECUCIÓN DEL CONTROLADOR (MAIN)      \n");
    printf("===============================================================\n");
    printf(" HILO RECURSO (FILÓSOFO)  |   VECES EJECUTADO (COMIDAS)  |  ESTADO \n");
    printf("--------------------------|------------------------------|---------\n");
    
    int total_ejecuciones = 0;
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        printf("   Filósofo Thread %d     |            %d / %d             |  ÉXITO \n", 
               i, contador_comidas[i], LIMITE_COMIDAS);
        total_ejecuciones += contador_comidas[i];
    }
    
    printf("---------------------------------------------------------------\n");
    printf(" TOTAL DE EJECUCIONES CONCURRENTES EN LA REGION CRITICA: %d\n", total_ejecuciones);
    printf("===============================================================\n");
    printf("[SISTEMA] Sincronización libre de Deadlock e Inanición demostrada.\n");

    // 5. Destruir semáforos
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        sem_destroy(&palillos[i]);
    }

    return 0;
}
