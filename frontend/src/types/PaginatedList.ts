import parseLinkHeader from "parse-link-header";

export class PaginatedList<T> {
    data: T[];
    first: string;
    last: string;
    prev?: string;
    next?: string;

    constructor(data: T[], linkHeader: string) {
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
        this.first = links.first.url;
        this.last = links.last.url;
        this.prev = links.prev?.url;
        this.next = links.next?.url;
    }
}
