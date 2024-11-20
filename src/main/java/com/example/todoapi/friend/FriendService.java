package com.example.todoapi.friend;

import com.example.todoapi.common.exception.BadRequestException;
import com.example.todoapi.common.message.ErrorMessage;
import com.example.todoapi.member.Member;
import com.example.todoapi.member.MemberRepository;
import com.example.todoapi.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    //친구 생성
    @Transactional
    public Long createFriend(Long memberId1, Long memberId2) throws Exception {
        Member member1 = memberRepository.findById(memberId1);
        Member member2 = memberRepository.findById(memberId2);

        if((member1 == null) || (member2 == null)) {
            throw new BadRequestException(ErrorMessage.MEMBER_NOT_EXISTS);
        }

        Friend friend = new Friend(member1, member2);
        friendRepository.save(friend);
        return friend.getId();
    }

    //단건 조회
    @Transactional(readOnly = true)
    public Friend findOneFriend(Long friendId) throws Exception {
        Friend friend = friendRepository.findById(friendId);

        if (friend == null) {
            throw new BadRequestException(ErrorMessage.FRIEND_NOT_EXISTS);
        }

        return friend;
    }

    //다건 조회(필요한 기능인가..?)
    @Transactional(readOnly = true)
    public List<Friend> getAllFriend() {
        return friendRepository.findAll();
    }

    //조건 조회(특정 멤버의 모든 친구 조회)
    @Transactional(readOnly = true)
    public List<Friend> getAllFriendByMember(Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId);

        if(member == null) {
            throw new BadRequestException(ErrorMessage.MEMBER_NOT_EXISTS);
        }

        return friendRepository.findAllByMember(member);
    }

    //수정은 구현 X

    //친구 삭제
    @Transactional
    public void deleteFriend(Long friendId, Long memberId) throws Exception {
        Friend friend = friendRepository.findById(friendId);
        Member member = memberRepository.findById(memberId);

        if (friend == null) {
            throw new BadRequestException(ErrorMessage.FRIEND_NOT_EXISTS);
        }

        if (member == null) {
            throw new BadRequestException(ErrorMessage.MEMBER_NOT_EXISTS);
        }

        if ((friend.getMember1() != member) && (friend.getMember2() != member)) {
            throw new BadRequestException(ErrorMessage.UNAUTHORIZED);
        }

        friendRepository.deleteById(friendId);
    }
}
