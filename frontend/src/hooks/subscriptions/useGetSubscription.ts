import { HttpStatusCode } from "axios";

import { useGet } from "../requests/useGet";

export interface GetSubscriptionInput {
    subscriptionUrl: string;
    userUrl: string;
}

export const useGetSubscription = () => {
    const { loading, callGet } = useGet();

    async function getSubscription({
        subscriptionUrl,
        userUrl,
    }: GetSubscriptionInput): Promise<HttpStatusCode> {
        const response = await callGet(subscriptionUrl, {}, true, {
            user: userUrl,
        });

        return response.status;
    }

    return { loading, getSubscription };
};
