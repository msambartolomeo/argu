export const TRANSLATIONS_EN = {
    navbar: {
        explore: "Explore",
        login: "Login",
        register: "Register",
        searchBar: "Search",
        createDebate: "Create Debate",
        moderator: "Become Moderator",
        profile: "Profile",
    },
    categories: {
        culture: "Culture",
        economics: "Economics",
        education: "Education",
        entertainment: "Entertainment",
        history: "History",
        literature: "Literature",
        politics: "Politics",
        religion: "Religion",
        science: "Science",
        technology: "Technology",
        world: "World",
        other: "Other",
    },
    landingPage: {
        about: {
            about: "About Argu",
            aboutText1:
                "Argu cuts through the noise typically associated with social media, making it easy to engage in focused discussion.",
            aboutText2:
                "Argu allows to visualize debates in a structural manner: starting from introductions, followed by the debaters' arguments and finished by each debaters' conclusion.",
            aboutText3:
                "Only one debater can argue at the time. The other one has to wait their turn!",
            participate: "Participate in debates!",
            participateText1:
                "Create your own debate or participate in another one.",
            participateText2: "Note that only moderators can use this feature.",
            participateText3:
                "If you aren't a moderator yet, become one by clicking the button above!",
            engage: "Engage in debates!",
            engageText1:
                "Subscribe to receive notifications of the debate you're interested in.",
            engageText2: "Like arguments when you agree with the debater.",
            engageText3: "Vote for the one you consider is winning the debate!",
        },
        explore: {
            title: "Explore debates",
            buttons: {
                culture: "Culture",
                economics: "Economics",
                education: "Education",
                entertainment: "Entertainment",
                history: "History",
                literature: "Literature",
                politics: "Politics",
                religion: "Religion",
                science: "Science",
                technology: "Technology",
                world: "World",
                other: "Other",
            },
        },
        trending: {
            title: "Trending",
        },
    },
    profile: {
        userDebates: "{{username}}'s debates",
        profilePhotoAlt: "Profile photo",
        editProfileImg: "Edit profile image",
        upload: "Upload",
        close: "Close",
        confirm: "Confirm",
        createdIn: "Created on",
        logout: "Logout",
        deleteAccount: "Delete account",
        areYouSure:
            "Are you sure you want to delete your account? This action cannot be undone.",
        introducePswd: "Introduce your password",
        debatesSubscribed: "Debates Subscribed",
        myDebates: "My Debates",
    },
    debate: {
        creator: "Creator",
        subscribedUsers: "Subscribed users",
        created: "Created",
        status: "Status",
        statuses: {
            statusOpen: "Open",
            statusClosed: "Closed",
            statusDeleted: "Deleted",
            statusClosing: "Closing",
            statusVoting: "Voting",
        },
        userDeleted: "[deleted]",
        close: "Close debate",
        deleteConfirmation: "Are you sure you want to delete this debate?",
        delete: "Delete debate",
        for: "For: ",
        against: "Against: ",
        subscribe: "Subscribe",
        unsubscribe: "Unsubscribe",
        subscribed: "Subscribed users: ",
        noArguments: "This debate does not have any arguments yet.",
        waitTurn: "You need to wait for your turn to post your argument.",
        needToLogin:
            "Hey! Would you like to participate in the discussion, vote for the winner, argument, like other people's arguments, and subscribe to debates? ",
        firstLogin: "You need to be logged-in in order to do that!",
        argument: {
            introduction: "Introduction",
            argument: "Argument",
            conclusion: "Conclusion",
            introMessage: "Please introduce your stance in this debate.",
            postIntro: "Post introduction!",
            argumentMessage: "Leave an argument here.",
            postArgument: "Post argument!",
            conclusionMessage: "Please conclude your argument in this debate.",
            postConclusion: "Post conclusion!",
            content: "Add your argument here:",
            image: "Upload Image",
        },
        votes: {
            votes: "Votes",
            voted: "Voted: ",
            changeVote: "Changed your mind?",
            unvote: "Unvote",
            noVotes: "No votes yet",
            votingEnds: "Voting will end on ",
            draw: "This debate resulted in a draw!",
            whoWins: "Who is the winner of the debate?",
            winner: "The winner of the debate is:",
        },
        chat: {
            title: "Discussion",
            message: "Message",
            send: "Send",
            noMessages: "There are no messages in this debate",
        },
        recommendedDebates: "Recommended debates",
    },
    login: {
        welcomeBack: "Welcome back :)",
        username: "Username: ",
        password: "Password: ",
        rememberMe: "Remember me",
        login: "Login",
        noAccount: "Don't have an account? ",
        register: "Register here!",
        errors: {
            usernameOrPasswordIncorrect: "Username or password incorrect",
            usernameEmpty: "Username cannot be empty",
            passwordEmpty: "Password cannot be empty",
            usernameTooLong: "Username cannot be longer than 64 characters",
            passwordTooLong: "Password cannot be longer than 100 characters",
        },
    },
    register: {
        firstTime: "First time? Welcome!",
        email: "Email: ",
        username: "Username: ",
        password: "Password: ",
        confirmPassword: "Confirm password: ",
        register: "Register",
        alreadyHaveAccount: "Already have an account? ",
        login: "Login here!",
        errors: {
            emailEmpty: "Email cannot be empty",
            emailTooLong: "Email cannot be longer than 100 characters",
            emailInvalid: "Please enter a valid email",
            usernameEmpty: "Username cannot be empty",
            usernameTooLong: "Username cannot be longer than 64 characters",
            passwordEmpty: "Password cannot be empty",
            passwordTooLong: "Password cannot be longer than 100 characters",
            passwordsDontMatch: "Passwords do not match",
            usernameTaken: "Username already taken",
            emailTaken: "Email already taken",
        },
    },
    createDebate: {
        createDebate: "Create debate",
        title: "Debate title:",
        description: "Debate description:",
        position: "What is your position in this debate?",
        for: "For",
        against: "Against",
        opponentUsername: "Your opponent's username:",
        category: "Debate category:",
        categoryPlaceholder: "Select a category",
        image: "Debate image (optional):",
        uploadImage: "Upload Image",
        errors: {
            titleEmpty: "Title cannot be empty",
            titleTooLong: "Title cannot be longer than 100 characters",
            descriptionEmpty: "Description cannot be empty",
            descriptionTooLong:
                "Description cannot be longer than 1000 characters",
            opponentUsernameEmpty: "Opponent's username cannot be empty",
            opponentUsernameTooLong:
                "Opponent's username cannot be longer than 64 characters",
            opponentUsernameSame: "You cannot debate with yourself",
            categoryEmpty: "Category cannot be empty",
            imageTooBig: "Image cannot be bigger than 10MB",
            imageInvalid: "Image format is invalid",
        },
    },
    requestModerator: {
        requestModerator: "Want to become a moderator?",
        submit: "Send",
        message:
            "Once you become a moderator, you will be able to create your own debates. To prevent our users from misinformation, we ask you to tell us a bit about why you want to become one. We will review it and contact you as soon as possible.",
        label: "Explain why you should become a moderator",
        errors: {
            reasonEmpty: "You must provide a reason to become a moderator",
            reasonTooLong: "Your message cannot exceed 2000 characters",
        },
    },
    components: {
        deleteDialog: {
            yes: "Yes, I'm sure",
            cancel: "Cancel",
        },
        argumentBubble: {
            userSaid: "{{username}} said:",
            deleteConfirmation:
                "Are you sure you want to delete this argument?",
            deleted: "Argument deleted",
        },
        debatesList: {
            noDebates: "No debates found",
        },
    },
    discovery: {
        title: "Debates",
        categories: {
            title: "Categories",
            all: "All",
        },
        orderBy: {
            placeholders: {
                all: "All",
                newest: "Newest",
            },
            orderBy: {
                title: "Order by",
                dateDesc: "Newest",
                dateAsc: "Oldest",
                alphaAsc: "A - Z",
                alphaDesc: "Z - A",
                subsAsc: "Least subscribed",
                subsDesc: "Most subscribed",
            },
            status: {
                title: "Status",
                open: "Open",
                closed: "Closed",
                all: "All",
            },
            datePicker: {
                label: "Created since",
                placeholder: "Date",
                months: {
                    january: "January",
                    february: "February",
                    march: "March",
                    april: "April",
                    may: "May",
                    june: "June",
                    july: "July",
                    august: "August",
                    september: "September",
                    october: "October",
                    november: "November",
                    december: "December",
                },
                monthsShort: {
                    january: "Jan",
                    february: "Feb",
                    march: "Mar",
                    april: "Apr",
                    may: "May",
                    june: "Jun",
                    july: "Jul",
                    august: "Aug",
                    september: "Sep",
                    october: "Oct",
                    november: "Nov",
                    december: "Dec",
                },
                weekdays: {
                    sunday: "Sunday",
                    monday: "Monday",
                    tuesday: "Tuesday",
                    wednesday: "Wednesday",
                    thursday: "Thursday",
                    friday: "Friday",
                    saturday: "Saturday",
                },
                weekdaysShort: {
                    sunday: "Sun",
                    monday: "Mon",
                    tuesday: "Tue",
                    wednesday: "Wed",
                    thursday: "Thu",
                    friday: "Fri",
                    saturday: "Sat",
                },
                weekdaysAbbrev: {
                    sunday: "S",
                    monday: "M",
                    tuesday: "T",
                    wednesday: "W",
                    thursday: "T",
                    friday: "F",
                    saturday: "S",
                },
                cancel: "Cancel",
                clear: "Clear",
                done: "Ok",
            },
            showingAllDebates: "Showing all debates",
            showingDebatesInCategory:
                "Showing debates in category: {{category}}",
        },
    },
    error: {
        title: "Oops!",
        message: "An error has ocurred.",
        goBack: "Go back to the homepage",
    },
};
