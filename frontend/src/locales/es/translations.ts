export const TRANSLATIONS_ES = {
    navbar: {
        explore: "Explorar",
        login: "Ingresar",
        register: "Registrarse",
        searchBar: "Buscar",
        createDebate: "Crear Debate",
        moderator: "Ser Moderador",
        profile: "Perfil",
    },
    landingPage: {
        about: {
            about: "Acerca de Argu",
            aboutText1:
                "Argu traspasa el embrollo asociado con las redes sociales, resultando más fácil participar y centrarse en el debate.",
            aboutText2:
                "Argu permite visualizar debates de manera estructurada: comenzando con las introducciones, seguidos de los argumentos de los debatientes y finalizando con sus conclusiones.",
            aboutText3:
                "Solamente un debatiente puede dar argumentos por turno. ¡El otro participante debe esperar su turno!",
            participate: "¡Participá en debates!",
            participateText1: "Creá tu propio debate o participá en otro.",
            participateText2:
                "Notá que sólo moderadores pueden usar esta función.",
            participateText3:
                "Si todavía no sos moderador, ¡convertite en uno haciendo click en el botón de arriba!",
            engage: "Elegí tus favoritos",
            engageText1:
                "Suscribite para recibir notificaciones del debate que te interese.",
            engageText2: "Dale like a los argumentos con los que coincidís.",
            engageText3: "¡Votá a la persona que está ganando el debate!",
        },
        explore: {
            title: "Explorar debates",
            buttons: {
                culture: "Cultura",
                economics: "Economía",
                education: "Educación",
                entertainment: "Entretenimiento",
                history: "Historia",
                literature: "Literatura",
                politics: "Política",
                religion: "Religión",
                science: "Ciencia",
                technology: "Tecnología",
                world: "Mundo",
                other: "Otro",
            },
        },
        trending: {
            title: "Tendencias",
        },
    },
    profile: {
        userDebates: "Debates de {{username}}",
        profilePhotoAlt: "Foto de perfil",
        editProfileImg: "Editar foto de perfil",
        upload: "Subir",
        close: "Cerrar",
        confirm: "Confirmar",
        createdIn: "Creado el",
        logout: "Cerrar sesión",
        deleteAccount: "Eliminar cuenta",
        areYouSure:
            "¿Estás seguro de que quieres borrar la cuenta? Esta acción no se puede deshacer.",
        introducePswd: "Introduce tu contraseña",
        debatesSubscribed: "Debates suscriptos",
        myDebates: "Mis debates",
    },
    debate: {
        creator: "Creador",
        subscribedUsers: "Usuarios suscriptos",
        created: "Creado",
        status: "Estado",
        statusOpen: "Abierto",
        statusClosed: "Cerrado",
        statusDeleted: "Eliminado",
        statusClosing: "Cerrando",
        statusVoting: "Votando",
        userDeleted: "[usuario eliminado]",
        close: "Cerrar debate",
        deleteConfirmation: "¿Estás seguro de borrar este debate?",
        delete: "Borrar debate",
        for: "A favor: ",
        against: "En contra: ",
        subscribe: "Suscribirse",
        unsubscribe: "Desuscribirse",
        subscribed: "Usuarios suscriptos: ",
        noArguments: "Este debate todavía no tiene argumentos.",
        waitTurn: "Debes esperar a tu turno para dar tu argumento.",
        needToLogin:
            "¡Hola! ¿Te gustaría poder participar en la discusión, argumentar, darle me gusta a los argumentos de \
              otros, votar por quien crees que es el ganador de un debate y suscribirte a debates? ",
        firstLogin: "¡Primero debes iniciar sesión!",
        argument: {
            introduction: "Introducción",
            argument: "Argumentos",
            conclusion: "Conclusión",
            introMessage: "Por favor, introduzca su postura en este debate.",
            postIntro: "¡Enviar Introducción!",
            argumentMessage: "Deje su argumento.",
            postArgument: "¡Enviar Argumento!",
            conclusionMessage: "Por favor, concluya su postura en este debate.",
            postConclusion: "¡Enviar Conclusión!",
            content: "Escribe tu argumento:",
            image: "Subir Imagen",
        },
        votes: {
            votes: "Votos",
            voted: "Votaste a: ",
            changeVote: "¿Cambiaste de opinión?",
            unvote: "Cambiar voto",
            noVotes: "Aún no hay votos en este debate",
            votingEnds: "La votación concluirá el ",
            draw: "¡Este debate resultó en un empate!",
            whoWins: "¿Quién es el ganador del debate?",
            winner: "El ganador de este debate es: ",
        },
        chat: {
            title: "Discusión",
            message: "Mensaje",
            send: "Enviar",
            noMessages: "No hay mensajes en este debate",
        },
        recommendedDebates: "Debates recomendados",
    },
    login: {
        welcomeBack: "Bienvenido de nuevo :)",
        username: "Nombre de usuario: ",
        password: "Contraseña: ",
        rememberMe: "Recordarme",
        login: "Ingresar",
        noAccount: "¿No tienes cuenta? ",
        register: "¡Regístrate aquí!",
    },
    register: {
        firstTime: "¿Primera vez? ¡Bienvenido!",
        email: "Email: ",
        username: "Nombre de usuario: ",
        password: "Contraseña: ",
        confirmPassword: "Confirmar Contraseña: ",
        register: "Registrarse",
        alreadyHaveAccount: "¿Ya tienes una cuenta? ",
        login: "¡Ingresa aquí!",
    },
    createDebate: {
        createDebate: "Crear debate",
        title: "Descripción del debate:",
        description: "Debate description:",
        position: "¿Cuál es su posición en el debate?",
        for: "A favor",
        against: "En contra",
        opponentUsername: "Nombre de usuario de tu oponente:",
        category: "Categoría del debate:",
        image: "Imagen del debate (opcional):",
        uploadImage: "Subir foto",
    },
    requestModerator: {
        requestModerator: "¿Quieres ser moderador?",
        submit: "Enviar",
        message:
            "Cuando te conviertas en moderador, podrás crear tus propios debates. Para proteger a nuestros usuarios de la desinformación, te pediremos que nos cuentes la razón por la cuál quieres ser moderador. Lo revisaremos y te notificaremos cuando seas aceptado.",
        label: "Explica por qué deberías ser un moderador",
    },
    components: {
        delete: {
            yes: "Sí, estoy seguro",
            cancel: "Cancelar",
        },
        argumentBubble: {
            userSaid: "{{username}} dijo:",
            deleteConfirmation: "¿Estás seguro de borrar este argumento?",
            deleted: "Argumento eliminado",
        },
        debatesList: {
            noDebates: "No se encontraron debates",
        },
    },
    discovery: {
        title: "Debates",
        categories: {
            title: "Categorías",
            all: "Todos",
            culture: "Cultura",
            economics: "Economía",
            education: "Educación",
            entertainment: "Entretenimiento",
            history: "Historia",
            literature: "Literatura",
            politics: "Política",
            religion: "Religión",
            science: "Ciencia",
            technology: "Tecnología",
            world: "Mundo",
            other: "Otros",
        },
        orderBy: {
            orderBy: {
                title: "Ordenar por",
                dateDesc: "Más reciente",
                dateAsc: "Más antiguo",
                alphaAsc: "A - Z",
                alphaDesc: "Z - A",
                subsAsc: "Menos suscriptos",
                subsDesc: "Más suscriptos",
            },
            status: {
                title: "Estado",
                open: "Abierto",
                closed: "Cerrado",
                all: "Todos",
            },
            showingAllDebates: "Mostrando todos los debates",
            showingDebatesInCategory:
                "Mostrando debates en la categoría: {{category}}",
        },
    },
    error: {
        title: "¡Ups!",
        goBack: "Volver a inicio.",
    },
};