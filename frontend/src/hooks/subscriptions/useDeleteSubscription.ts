import { HttpStatusCode } from "axios";
import { useDelete } from "../requests/useDelete";

export interface DeleteSubscriptionInput {
    subscriptionUrl: string;
    // TODO: Maybe get the userUrl from the context
    userUrl: string;
}

export const useDeleteSubscription = () => {
    const { loading, callDelete } = useDelete();

    async function callDeleteSubscription({
        subscriptionUrl,
        userUrl,
    }: DeleteSubscriptionInput): Promise<HttpStatusCode> {
        const response = await callDelete(subscriptionUrl, {}, true, {
            user: userUrl,
        });
        return response.status;
    }

    return { loading, callDeleteSubscription };
};
