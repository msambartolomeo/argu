/* eslint-disable camelcase */
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
    categories: {
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
        deleteSuccess: "Tu cuenta ha sido eliminada exitosamente.",
        introducePswd: "Introduce tu contraseña",
        debatesSubscribed: "Debates suscriptos",
        myDebates: "Mis debates",
        imageEmpty: "El archivo ingresado no es una imagen.",
        imageTooBig: "El arhivo ingresado supera el limite de tamaño (10MB).",
    },
    debate: {
        creator: "Creador",
        subscribedUsers: "Usuarios suscriptos",
        created: "Creado",
        status: "Estado",
        statuses: {
            statusOpen: "Abierto",
            statusClosed: "Cerrado",
            statusDeleted: "Eliminado",
            statusClosing: "Cerrando",
            statusVoting: "Votando",
        },
        debatePhotoAlt: "Foto del debate",
        userDeleted: "[usuario eliminado]",
        close: "Cerrar debate",
        deleteConfirmation: "¿Estás seguro de borrar este debate?",
        delete: "Borrar debate",
        deleteSuccess: "El debate ha sido eliminado exitosamente.",
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
        notParticipating:
            "No podés argumentar en este debate porque no estás participando.",
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
            errors: {
                messageEmpty: "Escriba un mensaje para participar",
                messageTooLong:
                    "El mensaje no puede ser más largo que 2000 caracteres",
            },
        },
        recommendedDebates: "Debates recomendados",
    },
    login: {
        welcomeBack: "Bienvenido de nuevo :)",
        username: "Nombre de usuario: ",
        password: "Contraseña: ",
        login: "Ingresar",
        noAccount: "¿No tienes cuenta? ",
        register: "¡Regístrate aquí!",
        errors: {
            usernameOrPasswordIncorrect:
                "Nombre de usuario o contraseña incorrectos",
            usernameEmpty: "El nombre de usuario no puede estar vacío",
            passwordEmpty: "La contraseña no puede estar vacía",
            usernameTooLong:
                "El nombre de usuario no puede ser más largo que 64 caracteres",
            passwordTooLong:
                "La contraseña no puede ser más larga que 100 caracteres",
        },
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
        errors: {
            emailEmpty: "La dirección de correo no puede estar vacía",
            emailTooLong:
                "La dirección de correo no puede ser más larga que 64 caracteres",
            emailInvalid: "Por favor ingrese una dirección de correo válida",
            usernameEmpty: "El nombre de usuario no puede estar vacío",
            usernameTooLong:
                "El nombre de usuario no puede ser más largo que 64 caracteres",
            passwordEmpty: "La contraseña no puede estar vacía",
            passwordTooLong:
                "La contraseña no puede ser más larga que 100 caracteres",
            passwordsDontMatch: "Las contraseñas no coinciden",
            usernameTaken: "El nombre de usuario ya está en uso",
            emailTaken: "La dirección de correo ya está en uso",
        },
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
        categoryPlaceholder: "Seleccione una categoría",
        image: "Imagen del debate (opcional):",
        uploadImage: "Subir foto",
        success: "¡Debate creado con éxito!",
        errors: {
            titleEmpty: "El título no puede estar vacío",
            titleTooLong: "El título no puede superar los 100 caracteres",
            descriptionEmpty: "La descripción no puede estar vacía",
            descriptionTooLong:
                "La descripción no puede superar los 1000 caracteres",
            opponentUsernameEmpty: "Debe especificar un oponente",
            opponentUsernameTooLong:
                "El nombre de usuario del oponente no puede superar los 64 caracteres",
            opponentUsernameSame: "No puedes ser tu propio oponente",
            categoryEmpty: "La categoría no puede estar vacía",
            imageTooBig: "La imagen no puede superar los 10MB",
            imageInvalid: "El formato de la imagen no es válido",
            opponentUsernameNotFound: "El oponente seleccionado no existe",
            isCreatorForEmpty: "Debe especificar una posición",
            isCreatorForInvalid: "Posición inválida",
        },
    },
    requestModerator: {
        title: "Solicitud de moderador",
        requestModerator: "¿Quieres ser moderador?",
        submit: "Enviar",
        message:
            "Cuando te conviertas en moderador, podrás crear tus propios debates. Para proteger a nuestros usuarios de la desinformación, te pediremos que nos cuentes la razón por la cuál quieres ser moderador. Lo revisaremos y te notificaremos cuando seas aceptado.",
        label: "Explica por qué deberías ser un moderador",
        errors: {
            reasonEmpty: "Debes especificar una razón para ser moderador",
            reasonTooLong: "Tu razón no puede superar los 2000 caracteres",
        },
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
            deleteSuccess: "Argumento eliminado exitosamente",
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
        },
        orderBy: {
            order: {
                title: "Ordenar por",
                date_desc: "Más reciente",
                date_asc: "Más antiguo",
                alpha_asc: "A - Z",
                alpha_desc: "Z - A",
                subs_asc: "Menos suscriptos",
                subs_desc: "Más suscriptos",
            },
            status: {
                title: "Estado",
                open: "Abierto",
                closed: "Cerrado",
                all: "Todos",
            },
            datePicker: {
                label: "Creado desde",
                placeholder: "Fecha",
                months: {
                    january: "Enero",
                    february: "Febrero",
                    march: "Marzo",
                    april: "Abril",
                    may: "Mayo",
                    june: "Junio",
                    july: "Julio",
                    august: "Agosto",
                    september: "Septiembre",
                    october: "Octubre",
                    november: "Noviembre",
                    december: "Diciembre",
                },
                monthsShort: {
                    january: "Ene",
                    february: "Feb",
                    march: "Mar",
                    april: "Abr",
                    may: "May",
                    june: "Jun",
                    july: "Jul",
                    august: "Ago",
                    september: "Sep",
                    october: "Oct",
                    november: "Nov",
                    december: "Dic",
                },
                weekdays: {
                    sunday: "Domingo",
                    monday: "Lunes",
                    tuesday: "Martes",
                    wednesday: "Miércoles",
                    thursday: "Jueves",
                    friday: "Viernes",
                    saturday: "Sábado",
                },
                weekdaysShort: {
                    sunday: "Dom",
                    monday: "Lun",
                    tuesday: "Mar",
                    wednesday: "Mié",
                    thursday: "Jue",
                    friday: "Vie",
                    saturday: "Sáb",
                },
                weekdaysAbbrev: {
                    sunday: "D",
                    monday: "L",
                    tuesday: "M",
                    wednesday: "M",
                    thursday: "J",
                    friday: "V",
                    saturday: "S",
                },
                cancel: "Cancelar",
                clear: "Limpiar",
                done: "Ok",
            },
            showingAllDebates: "Mostrando todos los debates",
            showingDebatesInCategory:
                "Mostrando debates en la categoría: {{category}}",
        },
        noDebates: {
            arguTeam: "El equipo de Argu dijo",
            arguTeamText: "Aún no hay debates para esta búsqueda",
            arguCommunity: "La comunidad de Argu dijo",
            arguCommunityText:
                "¡Pero podés crear tu propio debate y así ayudar a la comunidad a crecer!",
        },
    },
    errors: {
        title: "¡Ups!",
        message: "Ha ocurrido un error.",
        goBack: "Volver a inicio.",
        notFound: {
            page: "Página no encontrada",
            debate: "Debate no encontrado",
            user: "Usuario no encontrado",
        },
        conflict: {
            debateClosed: "El debate está cerrado, la acción no es posible",
        },
        unexpected: "An unexpected error occurred, please try again later",
    },
};
