import { HttpStatusCode } from "axios";

import { usePost } from "../requests/usePost";

export interface CreateSubscriptionInput {
    subscriptionUrl: string;
    // TODO: Maybe get the userUrl from the context
    userUrl: string;
}

export const useCreateSubscription = () => {
    const { loading, callPost } = usePost();

    async function createSubscription({
        subscriptionUrl,
        userUrl,
    }: CreateSubscriptionInput): Promise<HttpStatusCode> {
        const response = await callPost(subscriptionUrl, {}, {}, true, {
            user: userUrl,
        });
        return response.status;
    }

    return { loading, createSubscription };
};
