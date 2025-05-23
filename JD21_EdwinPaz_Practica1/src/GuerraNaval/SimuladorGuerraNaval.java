import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Ejercio 1 Practica
 * En un simulador de guerra naval, cada barco tiene una capacidad máxima de tripulantes y
 * está representado por una matriz de celdas (barco x tripulantes).
 * Cada celda puede contener un tripulante con un rol específico: Capitán, Artillero, Ingeniero, etc.
 * Tu tarea es:
 * Representar un barco con una matriz.
 * Simular la ocupación progresiva por tripulantes.
 * Detectar en qué momento el barco alcanza su capacidad máxima.
 * Permitir búsquedas por tipo de rol y retornar su ubicación.
 */
public class SimuladorGuerraNaval {

    // Constantes
    static final int MAX_BARCOS = 10;

    // Estructura para almacenar la información de los barcos
    static Map<Integer, Map<String, Object>> barcos = new HashMap<>();

    // Estructura para almacenar los tripulantes de cada barco
    // barcoTripulantes.get(idBarco) -> Map<Integer, Map<String, String>>
    // donde la clave es el número de posición y el valor es otro Map con "nombre" y "rol"
    static Map<Integer, Map<Integer, Map<String, String>>> barcoTripulantes = new HashMap<>();

    static int proximoIdBarco = 0;
    static int barcoActual = -1; // ID del barco seleccionado actualmente

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SIMULADOR DE GUERRA NAVAL ===");

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    registrarNuevoBarco(scanner);
                    break;
                case 2:
                    seleccionarBarco(scanner);
                    break;
                case 3:
                    if (barcoSeleccionadoValido()) {
                        gestionarBarcoSeleccionado(scanner);
                    } else {
                        System.out.println("Primero debe seleccionar un barco.");
                    }
                    break;
                case 4:
                    listarBarcos();
                    break;
                case 5:
                    ejecutarDemostracion();
                    break;
                case 6:
                    System.out.println("Saliendo del simulador...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 6);

        scanner.close();
    }

    /**
     * Muestra el menú principal
     */
    static void mostrarMenuPrincipal() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Registrar nuevo barco");
        System.out.println("2. Seleccionar barco");
        System.out.println("3. Gestionar barco seleccionado" +
                (barcoSeleccionadoValido() ? " (" + obtenerNombreBarco(barcoActual) + ")" : ""));
        System.out.println("4. Listar barcos registrados");
        System.out.println("5. Ejecutar demostración automática");
        System.out.println("6. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Registra un nuevo barco
     */
    static void registrarNuevoBarco(Scanner scanner) {
        if (barcos.size() >= MAX_BARCOS) {
            System.out.println("No se pueden registrar más barcos. Límite alcanzado.");
            return;
        }

        System.out.println("\n=== REGISTRO DE NUEVO BARCO ===");

        System.out.print("Nombre del barco: ");
        String nombre = scanner.nextLine();

        System.out.print("Capacidad máxima de tripulantes: ");
        int capacidadMaxima = Integer.parseInt(scanner.nextLine());

        // Crear un nuevo barco
        int idBarco = proximoIdBarco++;
        Map<String, Object> datosBarco = new HashMap<>();
        datosBarco.put("nombre", nombre);
        datosBarco.put("tripulantesActuales", 0);
        datosBarco.put("capacidadMaxima", capacidadMaxima);

        // Guardar el barco
        barcos.put(idBarco, datosBarco);

        // Inicializar el mapa de tripulantes para este barco
        barcoTripulantes.put(idBarco, new HashMap<>());

        // Seleccionar automáticamente el barco recién creado
        barcoActual = idBarco;

        System.out.println("Barco " + nombre + " registrado con éxito (ID: " + idBarco + ")");
        System.out.println("Capacidad: " + capacidadMaxima + " tripulantes");
    }

    /**
     * Selecciona un barco para gestionar
     */
    static void seleccionarBarco(Scanner scanner) {
        if (barcos.isEmpty()) {
            System.out.println("No hay barcos registrados. Registre un barco primero.");
            return;
        }

        listarBarcos();

        System.out.print("Seleccione el ID del barco: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (barcos.containsKey(id)) {
            barcoActual = id;
            System.out.println("Barco seleccionado: " + obtenerNombreBarco(barcoActual));
        } else {
            System.out.println("ID de barco no válido.");
        }
    }

    /**
     * Lista todos los barcos registrados
     */
    static void listarBarcos() {
        System.out.println("\n=== BARCOS REGISTRADOS ===");

        if (barcos.isEmpty()) {
            System.out.println("No hay barcos registrados.");
            return;
        }

        System.out.println("ID\tNombre\t\tCapacidad\tOcupación");
        System.out.println("--------------------------------------------------");

        for (Map.Entry<Integer, Map<String, Object>> entry : barcos.entrySet()) {
            int id = entry.getKey();
            Map<String, Object> datosBarco = entry.getValue();

            String nombre = (String) datosBarco.get("nombre");
            int tripulantesActuales = (int) datosBarco.get("tripulantesActuales");
            int capacidadMaxima = (int) datosBarco.get("capacidadMaxima");
            double porcentaje = (double) tripulantesActuales / capacidadMaxima * 100;

            System.out.println(id + "\t" + nombre + "\t\t" +
                    capacidadMaxima + "\t\t" +
                    tripulantesActuales + " (" +
                    String.format("%.1f", porcentaje) + "%)");
        }
    }

    /**
     * Gestiona el barco seleccionado actualmente
     */
    static void gestionarBarcoSeleccionado(Scanner scanner) {
        if (!barcoSeleccionadoValido()) {
            System.out.println("No hay un barco seleccionado.");
            return;
        }

        int opcion;
        do {
            mostrarMenuBarco();
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    agregarTripulante(scanner, barcoActual);
                    break;
                case 2:
                    mostrarEstadoBarco(barcoActual);
                    break;
                case 3:
                    buscarPorRol(scanner, barcoActual);
                    break;
                case 4:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 4);
    }

    /**
     * Muestra el menú de gestión de barco
     */
    static void mostrarMenuBarco() {
        System.out.println("\n=== GESTIÓN DEL BARCO: " + obtenerNombreBarco(barcoActual) + " ===");
        System.out.println("1. Agregar tripulante");
        System.out.println("2. Mostrar estado del barco");
        System.out.println("3. Buscar por rol");
        System.out.println("4. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Verifica si hay un barco seleccionado válido
     */
    static boolean barcoSeleccionadoValido() {
        return barcoActual >= 0 && barcos.containsKey(barcoActual);
    }

    /**
     * Agrega un tripulante al barco especificado
     */
    static void agregarTripulante(Scanner scanner, int idBarco) {
        if (estaLleno(idBarco)) {
            System.out.println("El barco está lleno. No se pueden agregar más tripulantes.");
            return;
        }

        System.out.print("Nombre del tripulante: ");
        String nombre = scanner.nextLine();

        System.out.print("Rol (Capitán, Artillero, Ingeniero, etc.): ");
        String rol = scanner.nextLine();

        boolean agregado = agregarTripulanteABarco(idBarco, nombre, rol);

        if (agregado) {
            System.out.println("Tripulante agregado. Ocupación: " +
                    String.format("%.1f", getPorcentajeOcupacion(idBarco)) + "%");
        } else {
            System.out.println("No se pudo agregar el tripulante. El barco está lleno.");
        }
    }

    /**
     * Agrega un tripulante al barco especificado en la siguiente posición disponible
     * retorna true si se pudo agregar, false si el barco está lleno
     */
    static boolean agregarTripulanteABarco(int idBarco, String nombre, String rol) {
        if (estaLleno(idBarco)) {
            return false;
        }

        Map<String, Object> datosBarco = barcos.get(idBarco);
        int tripulantesActuales = (int) datosBarco.get("tripulantesActuales");
        Map<Integer, Map<String, String>> tripulantes = barcoTripulantes.get(idBarco);

        // La posición del nuevo tripulante es el número actual + 1
        int posicion = tripulantesActuales + 1;

        // Crear datos del tripulante
        Map<String, String> datosTripulante = new HashMap<>();
        datosTripulante.put("nombre", nombre);
        datosTripulante.put("rol", rol);

        // Agregar tripulante
        tripulantes.put(posicion, datosTripulante);

        // Actualizar contador de tripulantes
        tripulantesActuales++;
        datosBarco.put("tripulantesActuales", tripulantesActuales);

        // Verificar si con este tripulante se alcanzó la capacidad máxima
        int capacidadMaxima = (int) datosBarco.get("capacidadMaxima");
        if (tripulantesActuales == capacidadMaxima) {
            System.out.println("¡ALERTA! El barco " + obtenerNombreBarco(idBarco) +
                    " ha alcanzado su capacidad máxima.");
        }

        return true;
    }

    /**
     * Muestra el estado actual del barco especificado
     */
    static void mostrarEstadoBarco(int idBarco) {
        Map<String, Object> datosBarco = barcos.get(idBarco);
        String nombre = (String) datosBarco.get("nombre");
        int tripulantesActuales = (int) datosBarco.get("tripulantesActuales");
        int capacidadMaxima = (int) datosBarco.get("capacidadMaxima");
        Map<Integer, Map<String, String>> tripulantes = barcoTripulantes.get(idBarco);

        System.out.println("\n=== Estado del Barco " + nombre + " ===");
        System.out.println("Capacidad: " + tripulantesActuales + "/" + capacidadMaxima);
        System.out.println("Ocupación: " + String.format("%.1f", getPorcentajeOcupacion(idBarco)) + "%");
        System.out.println("\nLista de Tripulantes:");
        System.out.println("--------------------------------------------------");

        if (tripulantesActuales == 0) {
            System.out.println("El barco no tiene tripulantes.");
        } else {
            System.out.println("Posición\tNombre\t\tRol");
            for (int i = 1; i <= tripulantesActuales; i++) {
                Map<String, String> tripulante = tripulantes.get(i);
                System.out.println(i + "\t\t" + tripulante.get("nombre") + "\t\t" + tripulante.get("rol"));
            }
        }

        System.out.println("--------------------------------------------------");

        // Mostrar posiciones disponibles
        if (tripulantesActuales < capacidadMaxima) {
            int disponibles = capacidadMaxima - tripulantesActuales;
            System.out.println("Posiciones disponibles: " + disponibles);
        } else {
            System.out.println("El barco está completamente ocupado.");
        }

        System.out.println("=====================================");
    }

    /**
     * Busca tripulantes por rol en el barco especificado y muestra sus ubicaciones
     */
    static void buscarPorRol(Scanner scanner, int idBarco) {
        System.out.print("Ingrese el rol a buscar: ");
        String rolBuscado = scanner.nextLine();

        Map<String, Object> datosBarco = barcos.get(idBarco);
        String nombreBarco = (String) datosBarco.get("nombre");
        Map<Integer, Map<String, String>> tripulantes = barcoTripulantes.get(idBarco);

        boolean encontrado = false;
        System.out.println("Tripulantes con rol '" + rolBuscado + "' encontrados en " + nombreBarco + ":");

        for (Map.Entry<Integer, Map<String, String>> entry : tripulantes.entrySet()) {
            int posicion = entry.getKey();
            Map<String, String> tripulante = entry.getValue();

            if (tripulante.get("rol").equalsIgnoreCase(rolBuscado)) {
                System.out.println("- Posición " + posicion + " - " + tripulante.get("nombre"));
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron tripulantes con el rol: " + rolBuscado);
        }
    }

    /**
     * Verifica si el barco especificado está lleno
     * retorna true si está lleno, false en caso contrario
     */
    static boolean estaLleno(int idBarco) {
        Map<String, Object> datosBarco = barcos.get(idBarco);
        int tripulantesActuales = (int) datosBarco.get("tripulantesActuales");
        int capacidadMaxima = (int) datosBarco.get("capacidadMaxima");

        return tripulantesActuales >= capacidadMaxima;
    }

    /**
     * Obtiene el porcentaje de ocupación del barco especificado
     * retorna el Porcentaje de ocupación
     */
    static double getPorcentajeOcupacion(int idBarco) {
        Map<String, Object> datosBarco = barcos.get(idBarco);
        int tripulantesActuales = (int) datosBarco.get("tripulantesActuales");
        int capacidadMaxima = (int) datosBarco.get("capacidadMaxima");

        return (double) tripulantesActuales / capacidadMaxima * 100;
    }

    /**
     * Obtiene el nombre de un barco por su ID
     */
    static String obtenerNombreBarco(int idBarco) {
        Map<String, Object> datosBarco = barcos.get(idBarco);
        return (String) datosBarco.get("nombre");
    }

    /**
     * Busca tripulantes por rol en toda la flota
     */
    static void buscarRolEnFlota(String rol) {
        System.out.println("Buscando rol '" + rol + "' en toda la flota:");
        boolean encontrado = false;

        for (Map.Entry<Integer, Map<Integer, Map<String, String>>> entryBarco : barcoTripulantes.entrySet()) {
            int idBarco = entryBarco.getKey();
            Map<Integer, Map<String, String>> tripulantes = entryBarco.getValue();
            String nombreBarco = obtenerNombreBarco(idBarco);

            for (Map.Entry<Integer, Map<String, String>> entryTripulante : tripulantes.entrySet()) {
                int posicion = entryTripulante.getKey();
                Map<String, String> tripulante = entryTripulante.getValue();

                if (tripulante.get("rol").equalsIgnoreCase(rol)) {
                    System.out.println("  - Barco: " + nombreBarco +
                            ", Posición " + posicion +
                            " - " + tripulante.get("nombre"));
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            System.out.println("  No se encontraron tripulantes con este rol en ningún barco.");
        }
    }

    /**
     * Ejecuta una demostración automática del simulador con múltiples barcos
     */
    static void ejecutarDemostracion() {
        // Reiniciar el sistema para la demostración
        barcos.clear(); //Limpia el objeto de barcos
        barcoTripulantes.clear(); //Limpia los tripulantes
        proximoIdBarco = 0;
        barcoActual = -1;

        System.out.println("\n=== DEMOSTRACIÓN AUTOMÁTICA ===");

        // Crear primer barco: Destructor
        int destructorId = proximoIdBarco++;
        Map<String, Object> datosDestructor = new HashMap<>();
        datosDestructor.put("nombre", "Destructor Pipil");
        datosDestructor.put("tripulantesActuales", 0);
        datosDestructor.put("capacidadMaxima", 12);
        barcos.put(destructorId, datosDestructor);
        barcoTripulantes.put(destructorId, new HashMap<>());

        System.out.println("1. Barco creado: Destructor Pipil (capacidad: 12 tripulantes)");

        // Crear segundo barco: Portaaviones
        int portaavionesId = proximoIdBarco++;
        Map<String, Object> datosPortaaviones = new HashMap<>();
        datosPortaaviones.put("nombre", "Portaaviones Trex");
        datosPortaaviones.put("tripulantesActuales", 0);
        datosPortaaviones.put("capacidadMaxima", 20);
        barcos.put(portaavionesId, datosPortaaviones);
        barcoTripulantes.put(portaavionesId, new HashMap<>());

        System.out.println("2. Barco creado: Portaaviones Trex (capacidad: 20 tripulantes)");

        // Agregar tripulantes al Destructor
        System.out.println("\n3. Agregando tripulantes al Destructor Alpha:");
        agregarYMostrar(destructorId, "Juan Pérez", "Capitán");
        agregarYMostrar(destructorId, "Ana Gómez", "Artillero");
        agregarYMostrar(destructorId, "Carlos Ruiz", "Ingeniero");
        agregarYMostrar(destructorId, "María López", "Navegante");

        // Agregar tripulantes al Portaaviones
        System.out.println("\n4. Agregando tripulantes al Portaaviones Omega:");
        agregarYMostrar(portaavionesId, "Roberto Díaz", "Capitán");
        agregarYMostrar(portaavionesId, "Sofía Martín", "Piloto");
        agregarYMostrar(portaavionesId, "Javier Núñez", "Ingeniero");
        agregarYMostrar(portaavionesId, "Elena Castro", "Médico");
        agregarYMostrar(portaavionesId, "Miguel Ángel", "Artillero");

        // Mostrar estado de ambos barcos
        System.out.println("\n5. Estado actual de los barcos:");
        mostrarEstadoBarco(destructorId);
        mostrarEstadoBarco(portaavionesId);

        // Buscar por rol en ambos barcos
        System.out.println("\n6. Buscando tripulantes por rol en toda la flota:");
        buscarRolEnFlota("Capitán");
        buscarRolEnFlota("Ingeniero");
        buscarRolEnFlota("Piloto");

        // Llenar el Destructor hasta su capacidad máxima
        System.out.println("\n7. Llenando el Destructor Alpha hasta su capacidad máxima:");
        agregarYMostrar(destructorId, "Pedro Sanchez", "Artillero");
        agregarYMostrar(destructorId, "Laura Torres", "Médico");
        agregarYMostrar(destructorId, "Fernando Gil", "Comunicaciones");
        agregarYMostrar(destructorId, "Lucia Vega", "Ingeniero");
        agregarYMostrar(destructorId, "Andres Mora", "Navegante");
        agregarYMostrar(destructorId, "Carmen Ruiz", "Artillero");
        agregarYMostrar(destructorId, "David Lopez", "Ingeniero");
        agregarYMostrar(destructorId, "Isabel Díaz", "Médico");

        // Intentar agregar un tripulante más al Destructor
        System.out.println("\n8. Intentando agregar un tripulante adicional al Destructor Alpha:");
        agregarYMostrar(destructorId, "Pablo Moreno", "Capitán");

        // Mostrar resumen final
        System.out.println("\n9. Resumen final de la flota:");
        listarBarcos();
    }

    /**
     * Método auxiliar para agregar un tripulante y mostrar el resultado en la demostración
     */
    static void agregarYMostrar(int idBarco, String nombre, String rol) {
        boolean resultado = agregarTripulanteABarco(idBarco, nombre, rol);
        System.out.println("- Agregando " + nombre + " (" + rol + ") a " + obtenerNombreBarco(idBarco) + ": " +
                (resultado ? "Éxito" : "Fracaso - Barco lleno") +
                " (Ocupación: " + String.format("%.1f", getPorcentajeOcupacion(idBarco)) + "%)");
    }
}