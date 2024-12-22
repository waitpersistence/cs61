class Account:
    """An account has a balance and a holder.

    >>> a = Account('John')
    >>> a.deposit(10)
    10
    >>> a.balance
    10
    >>> a.interest
    0.02
    >>> a.time_to_retire(10.25)  # 10 -> 10.2 -> 10.404
    2
    >>> a.balance                # Calling time_to_retire method should not change the balance
    10
    >>> a.time_to_retire(11)     # 10 -> 10.2 -> ... -> 11.040808032
    5
    >>> a.time_to_retire(100)
    117
    """
    max_withdrawal = 10
    interest = 0.02

    def __init__(self, account_holder):
        self.balance = 0
        self.holder = account_holder

    def deposit(self, amount):
        self.balance = self.balance + amount
        return self.balance

    def withdraw(self, amount):
        #print(2)
        if amount > self.balance:
            return "Insufficient funds"
        if amount > self.max_withdrawal:
            return "Can't withdraw that amount"
        self.balance = self.balance - amount
        return self.balance

    def time_to_retire(self, amount):
        """Return the number of years until balance would grow to amount."""
        assert self.balance > 0 and amount > 0 and self.interest > 0
        "*** YOUR CODE HERE ***"
        year=0
        while(self.balance*(1.02**(year))<amount):
            year+=1
        return year


class FreeChecking(Account):
    """A bank account that charges for withdrawals, but the first two are free!

    >>> ch = FreeChecking('Jack')
    >>> ch.balance = 20
    >>> ch.withdraw(100)  # First one's free. Still counts as a free withdrawal even though it was unsuccessful
    'Insufficient funds'
    >>> ch.withdraw(3)    # Second withdrawal is also free
    17
    >>> ch.balance
    17
    >>> ch.withdraw(3)    # Now there is a fee because free_withdrawals is only 2
    13
    >>> ch.withdraw(3)
    9
    >>> ch2 = FreeChecking('John')
    >>> ch2.balance = 10
    >>> ch2.withdraw(3) # No fee
    7
    >>> ch.withdraw(3)  # ch still charges a fee
    5
    >>> ch.withdraw(5)  # Not enough to cover fee + withdraw
    'Insufficient funds'
    """
    
    "*** YOUR CODE HERE ***"
    withdraw_fee = 1
    free_withdrawals = 2
    def __init__(self, account_holder):
        super().__init__(account_holder)
        self.withdrawals = 0

    def withdraw(self, amount):
        self.withdrawals += 1
        fee = 0
        if self.withdrawals > self.free_withdrawals:
            fee = self.withdraw_fee
        return super().withdraw(amount + fee)
#     def withdraw(self, amount):
# 	        if(self.count<=self.free_withdrawals):
# 	            super().withdraw(amount)#这里没有加return  
# 81	        else:
# 82	            #先计算够不够
# 83	            if(self.balance-amount-self.withdraw_fee<0):
# 84	                return "Insufficient funds"
# 85	            else:
# 86	                super().withdraw(amount)
# 87	                self.balance-=self.withdraw_fee
# 88	        self.count+=1
# ch = FreeChecking('Jack')
# # >>> ch.balance = 20
# # >>> ch.withdraw(100)  # First one's free. Still counts as a free withdrawal even though it was unsuccessful
# 在以上测试中错误，原因为：ch.withdraw(100)时，确实调用了super().withdraw(amount)，他返回的‘Insufficient funds’在freechecking中有
# 但是在这里就停止了，没有继续返回，相当于insufficient funds在这里只是一个实例变量，并没有被返回，这是一个层级问题。注意！！
                
    


def without(s, i):
    """Return a new linked list like s but without the element at index i.

    >>> s = Link(3, Link(5, Link(7, Link(9))))
    >>> without(s, 0)
    Link(5, Link(7, Link(9)))
    >>> without(s, 2)
    Link(3, Link(5, Link(9)))
    >>> without(s, 4)           # There is no index 4, so all of s is retained.
    Link(3, Link(5, Link(7, Link(9))))
    >>> without(s, 4) is not s  # Make sure a copy is created
    True
    """
    "*** YOUR CODE HERE ***"
    if(s is Link.empty):
        #两个边界条件
        #1.读到底了，返回空的
        return s
    if(i==0):
        #2.读到要跳过的返回剩下的，刷新用s.rest保存。
        return s.rest
    else:
        return Link(s.first,without(s.rest,i-1))#调用自身
    #这个递归值得学习
def duplicate_link(s, val):
    """Mutates s so that each element equal to val is followed by another val.

    >>> x = Link(5, Link(4, Link(5)))
    >>> duplicate_link(x, 5)
    >>> x
    Link(5, Link(5, Link(4, Link(5, Link(5)))))
    >>> y = Link(2, Link(4, Link(6, Link(8))))
    >>> duplicate_link(y, 10)
    >>> y
    Link(2, Link(4, Link(6, Link(8))))
    >>> z = Link(1, Link(2, (Link(2, Link(3)))))
    >>> duplicate_link(z, 2) # ensures that back to back links with val are both duplicated
    >>> z
    Link(1, Link(2, Link(2, Link(2, Link(2, Link(3))))))
    """
    "*** YOUR CODE HERE ***"
    if(s is Link.empty):
        return s
    if(s.first==val):#找到要复制的
        x=s.rest
        s.rest=Link(val)
        s.rest.rest=x
        #进行处理处理完后继续duplicate
        duplicate_link(s.rest.rest,val)
    else:
        duplicate_link(s.rest,val)


class Link:
    """A linked list.

    >>> s = Link(1)
    >>> s.first
    1
    >>> s.rest is Link.empty
    True
    >>> s = Link(2, Link(3, Link(4)))
    >>> s.first = 5
    >>> s.rest.first = 6
    >>> s.rest.rest = Link.empty
    >>> s                                    # Displays the contents of repr(s)
    Link(5, Link(6))
    >>> s.rest = Link(7, Link(Link(8, Link(9))))
    >>> s
    Link(5, Link(7, Link(Link(8, Link(9)))))
    >>> print(s)                             # Prints str(s)
    <5 7 <8 9>>
    """
    empty = ()

    def __init__(self, first, rest=empty):
        assert rest is Link.empty or isinstance(rest, Link)
        self.first = first
        self.rest = rest

    def __repr__(self):
        if self.rest is not Link.empty:
            rest_repr = ', ' + repr(self.rest)
        else:
            rest_repr = ''
        return 'Link(' + repr(self.first) + rest_repr + ')'

    def __str__(self):
        string = '<'
        while self.rest is not Link.empty:
            string += str(self.first) + ' '
            self = self.rest
        return string + str(self.first) + '>'
