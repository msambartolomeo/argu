import { HttpStatusCode } from "axios";

import { PaginatedList } from "./PaginatedList";

export default interface PaginatedOutput<T> {
    status: HttpStatusCode;
    data?: PaginatedList<T>;
}
