#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#define NUM_FILOSOFOS 5
// Arreglo de semáforos: cada semáforo representa un palillo (1 = libre, 0 = ocupado)
sem_t palillos[NUM_FILOSOFOS];
// Función que ejecuta cada hilo (filósofo)
void* filosofo(void* num) {
    int id = *(int*)num;
    while (1) {
        printf("Filósofo %d está pensando...\n", id);
        usleep(rand() % 1000000); // Piensa por un tiempo aleatorio
        printf("Filósofo %d tiene hambre…\n", id);
        // --- SOLUCIÓN PARA EVITAR DEADLOCK ---
        // Rompemos la simetría: El último filósofo toma los palillos en orden inverso
        if (id == NUM_FILOSOFOS - 1) {
            // Filósofo 4 toma primero el derecho (palillo 0) y luego el izquierdo (palillo 4)
            sem_wait(&palillos[(id + 1) % NUM_FILOSOFOS]);
            sem_wait(&palillos[id]);
        } else {
            // Los filósofos 0, 1, 2 y 3 toman primero el izquierdo y luego el derecho
            sem_wait(&palillos[id]);
            sem_wait(&palillos[(id + 1) % NUM_FILOSOFOS]);
        }
        // --- REGIÓN CRÍTICA: COMER ---
        printf("Filósofo %d está comiendo…\n", id);
        usleep(rand() % 1000000); // Come por un tiempo aleatorio
        // --- SALIDA DE LA REGIÓN CRÍTICA: SOLTAR PALILLOS ---
        printf("Filósofo %d terminó de comer y suelta sus palillos. \n", id);
        sem_post(&palillos[id]);
        sem_post(&palillos[(id + 1) % NUM_FILOSOFOS]);
      // Breve pausa antes de volver a empezar el ciclo
        usleep(100000);
    }
    return NULL;
}
int main() {
    pthread_t hilos[NUM_FILOSOFOS];
    int id_filosofos[NUM_FILOSOFOS];
    printf("[SISTEMA] Inicializando simulación de los Filósofos Comelones...\n");

    // 1. Inicializar los semáforos (palillos) en 1 (significa que están libres)
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        sem_init(&palillos[i], 0, 1);
    }
    // 2. Crear los hilos secundarios (los filósofos)
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        id_filosofos[i] = i;
        pthread_create(&hilos[i], NULL, filosofo, &id_filosofos[i]);
    }
    // 3. Esperar a los hilos (en este caso es infinito, se detiene con Ctrl+C)
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        pthread_join(hilos[i], NULL);
    }
    // 4. Destruir los semáforos al finalizar (buena práctica de SO)
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
        sem_destroy(&palillos[i]);
    }

    return 0;
}
