FinWise: App de Gestión de Gastos
FinWise es una aplicación de gestión financiera para Android, construida con tecnologías modernas de Kotlin y Jetpack Compose. La app permite a los usuarios registrarse, iniciar sesión y gestionar sus transacciones, gastos y balances, con una arquitectura MVVM limpia y escalable.

Tech Stack (Tecnologías Utilizadas)
Kotlin: Lenguaje de programación principal.

Jetpack Compose: UI 100% declarativa y moderna.

Arquitectura MVVM: (Model-View-ViewModel) para una clara separación de conceptos.

Jetpack Navigation: Para la navegación entre pantallas.

ViewModel: Manejo del estado de la UI y lógica de negocio.

Coroutines & StateFlow: Para manejo de concurrencia y estado asíncrono.

Retrofit: Para consumir la API REST (login, registro, datos de usuario).

Room: Para la base de datos local y caché offline de transacciones.

Repository Pattern: Como única fuente de verdad (Single Source of Truth).

✨ Funcionalidades Principales
Esta aplicación es un prototipo funcional que implementa los cuatro métodos principales de la API:

Registro de Usuario (/auth/create): Un usuario puede crear una cuenta nueva.

Login de Usuario (/auth/login): Un usuario puede iniciar sesión con sus credenciales.

Obtención de Datos del Usuario (/users/{id}): La app obtiene y muestra el balance y los datos del header del usuario logueado.

Obtención de Transacciones (/transactions): La app obtiene la lista de transacciones del usuario, las guarda en la base de datos (Room) y las muestra.

🚀 Cómo Probar la App (¡Importante!)
La aplicación tiene dos modos de inicio de sesión para demostrar su funcionalidad:

1. Inicio de Sesión Real (Usuario Registrado)
   Qué hacer: Ve a la pantalla de "Sign Up" y crea una cuenta nueva. Luego, vuelve a "Log In" e inicia sesión con esas credenciales.

Qué verás: Serás redirigido a la pantalla de "Home", pero verás que tu balance es $0.00 y no hay transacciones.

¿Por qué? ¡Esto es correcto! La API funciona y devuelve los datos de tu nueva cuenta, que está vacía.

2. Modo Invitado (Guest Mode) - RECOMENDADO
   Qué hacer: Para ver la aplicación en acción con todos los datos de muestra (gráficos, listas de transacciones, categorías, etc.), debes entrar como "Invitado".

¿Cómo entrar como invitado?

Ve a la pantalla de "Log In".

No escribas nada en los campos de "Email" o "Password". Déjalos vacíos.

Presiona el botón "Log In".

Qué verás: La app cargará un conjunto completo de datos de prueba (sample data) y podrás navegar por todas las pantallas (Transactions, Profile, Categories) y ver cómo funciona la UI.