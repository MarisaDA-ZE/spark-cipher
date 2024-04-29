/*
 * DemoSignal — Demonstrate the signal protocol.
 * Copyright (C) 2017 Vijay Lakshminarayanan <lvijay@gmail.com>.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package top.kirisamemarisa.sparkcipher.examples;

import com.alibaba.fastjson.JSONObject;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Demo {
    public static void main(String[] args) throws Exception {
        // 创建双方的实例
        Entity alice = new Entity(1, 314159, "alice");
        Entity bob = new Entity(2, 271828, "bob");

        System.out.println("JSON: >>>"+JSONObject.toJSONString(bob.getPreKey()));
        // 在双方之间建立一次会话
        Session aliceToBobSession = new Session(alice.getStore(), bob.getPreKey(), bob.getAddress());

        // alice 向 bob 发送消息
        List<PreKeySignalMessage> toBobMessages = Arrays.stream("31,41,59,26,53".split(","))
                .map(msg -> {
                    try {
                        return aliceToBobSession.encrypt(msg);
                    } catch (UntrustedIdentityException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());


        // bob如果想要阅读，那它就必须知道alice
        Session bobToAliceSession = new Session(bob.getStore(), alice.getPreKey(), alice.getAddress());

        // bob 解密数据
        String fromAliceMessages = toBobMessages.stream()
                .map(bobToAliceSession::decrypt)
                .collect(joining(","));

        System.out.println("alice解密数据: " + fromAliceMessages);

        // bob 向 alice 发送消息
        List<PreKeySignalMessage> toAliceMessages = Arrays.stream("the quick brown fox".split(" "))
                .map(msg -> {
                    try {
                        return bobToAliceSession.encrypt(msg);
                    } catch (UntrustedIdentityException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toList());

        // alice 也能解密 bob 的消息。即使他们到达时没有按顺序进行。
        Collections.shuffle(toAliceMessages);
        List<String> fromBobMessages = toAliceMessages.stream()
                .map(aliceToBobSession::decrypt)
                .collect(Collectors.toList());
        System.out.println("alice解密数据: " + fromBobMessages);
    }
}
