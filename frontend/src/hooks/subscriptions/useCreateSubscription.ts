import { HttpStatusCode } from "axios";

import { usePost } from "../requests/usePost";

export interface CreateSubscriptionInput {
    subscriptionUrl: string;
}

export const useCreateSubscription = () => {
    const { loading, callPost } = usePost();

    async function createSubscription({
        subscriptionUrl,
    }: CreateSubscriptionInput): Promise<HttpStatusCode> {
        const response = await callPost(subscriptionUrl, {}, {}, true);
        return response.status;
    }

    return { loading, createSubscription };
};
