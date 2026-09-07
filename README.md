[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=32&duration=3000&pause=500&color=FF0000&center=true&vCenter=true&width=800&lines=AutoMindX;Simulador+de+Aut%C3%B3matas+Finitos)](https://git.io/typing-svg)

<br>

[![Java](https://img.shields.io/badge/Java-25-ED8B00?logo=openjdk)](#)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?logo=bootstrap)](#)
[![HTML5](https://img.shields.io/badge/HTML5-E34F26?logo=html5)](#)
[![CSS3](https://img.shields.io/badge/CSS3-1572B6?logo=css3)](#)
[![Licencia](https://img.shields.io/badge/Licencia-MIT-green)](LICENSE)
[![Estado](https://img.shields.io/badge/Estado-Activo-brightgreen)](#)

> **Proyecto académico – Teoría de Autómatas y Lenguajes Formales**  
> *Simulador de autómatas finitos deterministas (DFA) con editor visual y validación animada*  
> *Arquitectura cliente-servidor con patrón MVC*  
> *Universidad El Bosque - Compiladores*

---

## Idea del proyecto

**AutoMindX** es una aplicación que permite **diseñar, visualizar y simular autómatas finitos deterministas (DFA)** de manera interactiva.  
El usuario puede **dibujar el grafo** directamente en un lienzo, definir el alfabeto, ingresar una cadena y **observar paso a paso el proceso de validación con colores dinámicos**, resaltando el estado inicial, la trayectoria recorrida y el resultado final (aceptada o rechazada).

La interfaz de usuario (vista) está construida con **HTML, CSS y Bootstrap 5**, que adapta el aplicativo al dispositivo del usuario. La lógica del autómata y la validación de cadenas están implementadas en **Java**, separadas claramente siguiendo el patrón **Modelo-Vista-Controlador (MVC)**.

El dibujo del grafo se realiza sobre un lienzo que utiliza **Java2D** para la representación gráfica, evitando dependencias externas para el pintado.

---

## Características principales

| Característica | Descripción |
|:---|:---|
| **Editor visual integrado** | Dibuja estados (círculos) y transiciones (flechas) con el ratón, estilo Paint. |
| **Interfaz moderna con Bootstrap** | Diseño responsivo y atractivo utilizando componentes y estilos de Bootstrap 5. |
| **Definición de alfabeto** | Permite especificar los símbolos aceptados por el autómata. |
| **Ingreso de cadena** | Campo para escribir la cadena que se desea validar. |
| **Validación dinámica** | Muestra con colores el proceso de recorrido: estado inicial, trayectoria y resultado final. |
| **Resaltado de trayectoria** | La ruta seguida se ilumina con un color distintivo durante la simulación. |
| **Estado inicial identificado** | El estado inicial se marca visualmente (por ejemplo, con una flecha entrante o color especial). |
| **Aceptación o rechazo visual** | Al finalizar, el autómata indica claramente si la cadena fue aceptada o rechazada, con colores representativos. |
| **Exportar/Importar** | Guarda y carga autómatas en formato JSON (opcional). |
| **dependencias externas para dibujo** | Solo Java estándar para el motor gráfico, sin bibliotecas adicionales. |

---

## Visualización del proceso de validación

AutoMindX no se limita a un simple "sí/no": ofrece una **experiencia visual interactiva** que muestra:

- **Estado inicial**: señalado con un borde especial o una flecha entrante.
- **Recorrido animado**: a medida que se consume cada símbolo de la cadena, el estado actual se resalta y la transición utilizada se ilumina.
- **Trayectoria completa**: al finalizar, la ruta completa queda resaltada, permitiendo ver exactamente cómo se llegó al resultado.
- **Resultado final**:
  - Aceptada: el último estado pertenece al conjunto de aceptación.
  - Rechazada: la cadena no puede completarse o termina en un estado no aceptador.

---

## Conceptos fundamentales de autómatas

| Concepto | Descripción |
|:---|:---|
| **Estado** | Nodo del grafo que representa una situación en el procesamiento de cadenas. |
| **Estado inicial** | Punto de partida del autómata; desde aquí se lee la cadena. |
| **Estado de aceptación** | Estados que, al finalizar la cadena, indican que ésta es válida. |
| **Transición** | Arco dirigido entre estados etiquetado con un símbolo del alfabeto. |
| **Alfabeto** | Conjunto de símbolos que el autómata reconoce. |
| **Cadena** | Secuencia de símbolos que se somete a validación. |

---

## Arquitectura del sistema

AutoMindX sigue una **arquitectura cliente-servidor** lógica, donde el cliente (vista web) se comunica con el servidor (lógica de negocio Java) a través del controlador. Se implementa el patrón **Modelo-Vista-Controlador (MVC)**:

- **Modelo**: Contiene la definición del autómata (estados, transiciones, alfabeto) y la lógica de validación de cadenas. Implementado en Java.
- **Vista**: Interfaz gráfica web construida con **HTML, CSS y Bootstrap 5**. Incluye el lienzo para dibujar el grafo y los paneles de control.
- **Controlador**: Maneja los eventos del usuario y coordina las acciones entre la vista y el modelo.
---

## Estructura del proyecto

```plaintext
AutoMindX/
├── src/
│   ├── main/
│   │   ├── java/com/automindx/
│   │   │   ├── modelo/          → Clases del modelo: Estado, Transicion, Automata, Validador
│   │   │   ├── controlador/     → Clases controladoras: ControladorEditor, ControladorSimulacion
│   │   │   ├── servicio/        → Lógica de negocio y comunicación con la vista
│   │   │   └── Main.java        → Punto de entrada 
│   │   └── resources/           → Recursos (iconos, fuentes)
├── web/
│   ├── css/                     → Estilos personalizados y Bootstrap
│   ├── js/                      → Lógica de interacción en el cliente
│   └── index.html               → Página principal de la vista
├── test/                        → Pruebas unitarias
├── docs/                        → Documentación y capturas
├── LICENSE
└── README.md
