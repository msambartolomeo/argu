import { UserInfo } from "../hooks/useAuth";
import ArgumentDto from "../types/dto/ArgumentDto";
import DebateDto from "../types/dto/DebateDto";
import UserDto from "../types/dto/UserDto";
import DebateCategory from "../types/enums/DebateCategory";
import DebateStatus from "../types/enums/DebateStatus";
import UserRole from "../types/enums/UserRole";

const mockedUser: UserDto = {
    username: "test",
    createdDate: "2021-10-10",
    points: 10,

    self: "http://localhost:8080/paw-2022a-06/users/test",
    image: "http://localhost:8080/paw-2022a-06/users/test/image",
    debates: "http://localhost:8080/paw-2022a-06/debates?userUrl=test",
    subscribedDebates:
        "http://localhost:8080/paw-2022a-06/debates?userUrl=test&subscribed=true",
};

const mockedUserInfo: UserInfo = {
    sub: "test",
    exp: 1000000000000,
    username: "test",
    email: "test@mail.com",
    role: UserRole.MODERATOR,
    points: 10,
};

const mockedDebate1: DebateDto = {
    id: 1,
    name: "Should we ban smoking in public places?",
    description:
        "Smoking is a major cause of preventable death and disease. It is estimated that smoking causes 1 in 5 deaths in the UK.",
    isCreatorFor: true,
    createdDate: new Date("2020-01-01"),
    dateToClose: "2020-01-05",
    category: DebateCategory.OTHER,
    status: DebateStatus.OPEN,
    subscriptionsCount: 10,
    votesFor: 5,
    votesAgainst: 5,
    creatorName: "test",
    opponentName: "test2",

    self: "http://localhost:8080/paw-2022a-06/debates/1",
    creator: "http://localhost:8080/paw-2022a-06/users/test",
    opponent: "http://localhost:8080/paw-2022a-06/users/test2",
    arguments: "http://localhost:8080/paw-2022a-06/debates/1/arguments",
    chats: "http://localhost:8080/paw-2022a-06/debates/1/chats",
    recommendations:
        "http://localhost:8080/paw-2022a-06/debates?recommendToDebate=1",
    sameCategory: "http://localhost:8080/paw-2022a-06/debates?category=other",
    sameStatus: "http://localhost:8080/paw-2022a-06/debates?status=open",
    afterSameDate: "http://localhost:8080/paw-2022a-06/debates?date=2020-01-05",
};

const mockedDebate2: DebateDto = {
    id: 2,
    name: "Should students use uniforms?",
    description:
        "School uniforms are a controversial topic in the United States. Some people believe that school uniforms are a good idea because they would reduce violence in schools, improve students' grades, and help students focus on their studies. Others believe that school uniforms are a bad idea because they would limit students' freedom of expression and take away students' individuality.",
    isCreatorFor: true,
    createdDate: new Date("2020-02-09"),
    dateToClose: "2020-02-12",
    category: DebateCategory.EDUCATION,
    status: DebateStatus.OPEN,
    subscriptionsCount: 15,
    votesFor: 5,
    votesAgainst: 8,
    creatorName: "test2",
    opponentName: "test",

    self: "http://localhost:8080/paw-2022a-06/debates/2",
    creator: "http://localhost:8080/paw-2022a-06/users/test2",
    opponent: "http://localhost:8080/paw-2022a-06/users/test",
    arguments: "http://localhost:8080/paw-2022a-06/debates/2/arguments",
    chats: "http://localhost:8080/paw-2022a-06/debates/2/chats",
    recommendations:
        "http://localhost:8080/paw-2022a-06/debates?recommendToDebate=2",
    sameCategory:
        "http://localhost:8080/paw-2022a-06/debates?category=education",
    sameStatus: "http://localhost:8080/paw-2022a-06/debates?status=open",
    afterSameDate: "http://localhost:8080/paw-2022a-06/debates?date=2020-02-09",
};

const mockedDebate3: DebateDto = {
    id: 3,
    name: "What came first, the chicken or the egg?",
    description:
        "The chicken or the egg is a common riddle that has been around for centuries. The question is, which came first, the chicken or the egg? The answer to this question is not as simple as it seems. The answer to this question depends on how you define the word 'first'.",
    isCreatorFor: false,
    createdDate: new Date("2020-10-19"),
    dateToClose: "2020-10-22",
    category: DebateCategory.SCIENCE,
    status: DebateStatus.OPEN,
    subscriptionsCount: 5,
    votesFor: 15,
    votesAgainst: 9,
    creatorName: "test2",
    opponentName: "test",

    self: "http://localhost:8080/paw-2022a-06/debates/3",
    creator: "http://localhost:8080/paw-2022a-06/users/test2",
    opponent: "http://localhost:8080/paw-2022a-06/users/test",
    arguments: "http://localhost:8080/paw-2022a-06/debates/3/arguments",
    chats: "http://localhost:8080/paw-2022a-06/debates/3/chats",
    recommendations:
        "http://localhost:8080/paw-2022a-06/debates?recommendToDebate=3",
    sameCategory: "http://localhost:8080/paw-2022a-06/debates?category=science",
    sameStatus: "http://localhost:8080/paw-2022a-06/debates?status=open",
    afterSameDate: "http://localhost:8080/paw-2022a-06/debates?date=2020-10-19",
};

const mockedArgument1: ArgumentDto = {
    content: "I think that smoking should be banned in public places.",
    createdDate: "2020-01-01",
    status: "Introduction",
    likes: 5,
    likedByUser: false,
    deleted: false,
    creatorName: "test",
    self: "http://localhost:8080/paw-2022a-06/debates/1/arguments/1",
    creator: "http://localhost:8080/paw-2022a-06/users/test",
    debate: "http://localhost:8080/paw-2022a-06/debates/1",
};

const mockedArgument2: ArgumentDto = {
    content: "I believe that smoking should not be banned in public places.",
    createdDate: "2020-01-01",
    status: "Introduction",
    likes: 2,
    likedByUser: false,
    deleted: false,
    creatorName: "test2",
    self: "http://localhost:8080/paw-2022a-06/debates/1/arguments/2",
    creator: "http://localhost:8080/paw-2022a-06/users/test2",
    debate: "http://localhost:8080/paw-2022a-06/debates/1",
};

const mockedArgument3: ArgumentDto = {
    content: "It is detrimental to the health of the people around you.",
    createdDate: "2020-01-01",
    status: "Argument",
    likes: 7,
    likedByUser: false,
    deleted: false,
    creatorName: "test",
    self: "http://localhost:8080/paw-2022a-06/debates/1/arguments/3",
    creator: "http://localhost:8080/paw-2022a-06/users/test",
    debate: "http://localhost:8080/paw-2022a-06/debates/1",
};

const mockedArgument4: ArgumentDto = {
    content: "If you are careful, you will not harm anyone.",
    createdDate: "2020-01-01",
    status: "Argument",
    likes: 5,
    likedByUser: true,
    deleted: false,
    creatorName: "test2",
    self: "http://localhost:8080/paw-2022a-06/debates/1/arguments/4",
    creator: "http://localhost:8080/paw-2022a-06/users/test2",
    debate: "http://localhost:8080/paw-2022a-06/debates/1",
};

const mockedArgument5: ArgumentDto = {
    content: "It is addictive and harmful to your health.",
    createdDate: "2020-01-02",
    status: "Conclusion",
    likes: 8,
    likedByUser: true,
    deleted: false,
    creatorName: "test",
    self: "http://localhost:8080/paw-2022a-06/debates/1/arguments/5",
    creator: "http://localhost:8080/paw-2022a-06/users/test",
    debate: "http://localhost:8080/paw-2022a-06/debates/1",
};

const mockedArgument6: ArgumentDto = {
    content: "It is not addictive and it is not harmful to your health.",
    createdDate: "2020-01-02",
    status: "Conclusion",
    likes: 2,
    likedByUser: false,
    deleted: false,
    creatorName: "test2",
    self: "http://localhost:8080/paw-2022a-06/debates/1/arguments/6",
    creator: "http://localhost:8080/paw-2022a-06/users/test2",
    debate: "http://localhost:8080/paw-2022a-06/debates/1",
};

export {
    mockedUser,
    mockedUserInfo,
    mockedDebate1,
    mockedDebate2,
    mockedDebate3,
    mockedArgument1,
    mockedArgument2,
    mockedArgument3,
    mockedArgument4,
    mockedArgument5,
    mockedArgument6,
};
