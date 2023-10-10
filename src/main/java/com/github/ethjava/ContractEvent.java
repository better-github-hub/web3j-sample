package com.github.ethjava;

import com.alibaba.fastjson.JSONObject;
import com.github.ethjava.utils.Environment;
import com.google.common.collect.Lists;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.http.HttpService;

import java.util.Arrays;
import java.util.List;

/**
 * Event log相关
 * 监听合约event
 */
public class ContractEvent {
    private static String contractAddress = "0x0528e86e09ea18d60b2a4e024315438dc8202f5d";
    private static Web3j web3j;

    public static void main(String[] args) {
        web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        /**
         * 监听ERC20 token 交易
         */
        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                contractAddress);
        Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        }, new TypeReference<Uint256>(false) {
                        }
                )
        );

        String topicData = EventEncoder.encode(event);
        filter.addSingleTopic(topicData);
        System.out.println(topicData);


        final Event TRANSFER_EVENT = new Event("Transfer", Arrays.asList(new TypeReference<Address>(true) {
        }, new TypeReference<Address>(true) {
        }, new TypeReference<Uint256>(false) {
        }));
// Event definition hash
        final String TRANSFER_EVENT_HASH = EventEncoder.encode(TRANSFER_EVENT);

        web3j.ethLogFlowable(filter).subscribe(log -> {
            System.out.println("log:" + JSONObject.toJSONString(log));
            System.out.println("blockNumber:" + log.getBlockNumber());
            System.out.println("transactionHash: " + log.getTransactionHash());
            System.out.println("log.getTopics: " + JSONObject.toJSONString(log.getTopics()));
            List<String> topics = log.getTopics();
            for (String topic : topics) {
                System.out.println(topic);
            }
            System.out.println("log:" + JSONObject.toJSONString(log));
            String eventHash = log.getTopics().get(0);
            if (eventHash.equals(TRANSFER_EVENT_HASH)) { // Only MyEvent. You can also use filter.addSingleTopic(MY_EVENT_HASH)
                Address arg1 = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {
                });
                Address arg2 = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(2), new TypeReference<Address>() {
                });
                System.out.println("from:" + arg1);
                System.out.println("to :" + arg2);
                List<Type> results = FunctionReturnDecoder.decode(log.getData(), TRANSFER_EVENT.getNonIndexedParameters());
                Type type = results.get(0);
                String value = type.getValue().toString();
                System.out.println("amount: " + value.toString());
            }
        });
    }
}
