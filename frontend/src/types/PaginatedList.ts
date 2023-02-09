import parseLinkHeader from "parse-link-header";

export class PaginatedList<T> {
    data: T[];
    first: string;
    last: string;
    prev?: string;
    next?: string;
    lastElement?: string;
    totalPages: number;
    lastArgument?: string;

    constructor(data: T[], linkHeader: string, totalPagesHeader: string) {
        this.data = data;

        const links = parseLinkHeader(linkHeader);
        if (!links) {
            throw new Error("Link header is missing");
        }
        if (!links.first) {
            throw new Error('Link header is missing "first" link');
        }
        if (!links.last) {
            throw new Error('Link header is missing "last" link');
        }

        this.lastElement = links.last_element?.url;
        this.first = links.first.url;
        this.last = links.last.url;
        this.prev = links.prev?.url;
        this.next = links.next?.url;
        this.lastArgument = links.last_element?.url;

        this.totalPages = Number(totalPagesHeader);
    }

    static emptyList(): PaginatedList<never> {
        return {
            data: [],
            first: "",
            last: "",
            totalPages: 0,
        };
    }
}
