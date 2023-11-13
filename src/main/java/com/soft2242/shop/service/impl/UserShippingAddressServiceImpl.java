package com.soft2242.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soft2242.shop.common.exception.ServerException;
import com.soft2242.shop.convert.AddressConvert;
import com.soft2242.shop.entity.UserShippingAddress;
import com.soft2242.shop.enums.AddressDefaultEnum;
import com.soft2242.shop.mapper.UserShippingAddressMapper;
import com.soft2242.shop.service.UserShippingAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft2242.shop.vo.AddressVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ycshang
 * @since 2023-11-07
 */
@Service
public class UserShippingAddressServiceImpl extends ServiceImpl<UserShippingAddressMapper, UserShippingAddress>
    implements UserShippingAddressService {

    /**
     * 添加收获地址
     *
     * @param addressVO
     * @return
     */
    @Override
    public Integer saveShippingAddress(AddressVO addressVO) {
//        System.out.println(addressVO);
        UserShippingAddress convert = AddressConvert.INSTANCE.convert(addressVO);
        if (addressVO.getIsDefault() == AddressDefaultEnum.DEFAULT_ADDRESS.getValue()) {
            List<UserShippingAddress> list = baseMapper.selectList(new LambdaQueryWrapper<UserShippingAddress>()
                .eq(UserShippingAddress::getIsDefault, AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            if (list.size() > 0) {
                throw new ServerException("已存在默认地址，请勿重复操作");
            }
        }
        save(convert);
        return convert.getId();
    }

    /**
     * 修改收货地址
     *
     * @param addressVO
     * @return
     */
    @Override
    public Integer editShippingAddress(AddressVO addressVO) {
        UserShippingAddress userShippingAddress = baseMapper.selectById(addressVO.getId());
        if (userShippingAddress == null) {
            throw new ServerException("地址不存在");
        }
        if (addressVO.getIsDefault() == AddressDefaultEnum.DEFAULT_ADDRESS.getValue()) {
            UserShippingAddress address = baseMapper.selectOne(
                new LambdaQueryWrapper<UserShippingAddress>().eq(UserShippingAddress::getUserId, addressVO.getUserId())
                    .eq(UserShippingAddress::getIsDefault, AddressDefaultEnum.DEFAULT_ADDRESS.getValue()));
            if (address != null) {
                address.setIsDefault((byte)(AddressDefaultEnum.NOT_DEFAULT_ADDRESS.getValue()).intValue());
                updateById(address);
            }
        }
        UserShippingAddress address = AddressConvert.INSTANCE.convert(addressVO);
        updateById(address);
        return address.getId();
    }

    /**
     * 获取收货地址列表
     *
     * @return
     */
    @Override
    public List<AddressVO> getUserShippingAddressList(Integer userId) {
        LambdaQueryWrapper<UserShippingAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserShippingAddress::getUserId, userId);
        wrapper.orderByDesc(UserShippingAddress::getIsDefault);
        wrapper.orderByDesc(UserShippingAddress::getCreateTime);
        List<UserShippingAddress> list = baseMapper.selectList(wrapper);
        List<AddressVO> addressVOList = AddressConvert.INSTANCE.convertToAddressVOList(list);
        return addressVOList;
    }

    /**
     * 根据id删除收货地址
     *
     * @param id
     * @return
     */
    @Override
    public void removeShippingAddress(Integer id) {
        removeById(id);
    }

    /**
     * 根据Id获取收货地址详情
     *
     * @param id
     * @return
     */
    @Override
    public AddressVO getAddressInfoById(Integer id) {
        UserShippingAddress userShippingAddress = baseMapper.selectById(id);
        if (userShippingAddress == null) {
            throw new ServerException("地址不存在");
        }
        AddressVO addressVO = AddressConvert.INSTANCE.convertToAddressVO(userShippingAddress);
        return addressVO;
    }

}
