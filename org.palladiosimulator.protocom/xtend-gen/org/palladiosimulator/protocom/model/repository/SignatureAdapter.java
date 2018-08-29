package org.palladiosimulator.protocom.model.repository;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.palladiosimulator.pcm.repository.InfrastructureSignature;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.protocom.model.ModelAdapter;

/**
 * Adapter class for PCM Signature entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class SignatureAdapter extends ModelAdapter<Signature> {
  public SignatureAdapter(final Signature entity) {
    super(entity);
  }
  
  /**
   * Gets the name of the service.
   * @return a string containing the name of the service
   */
  public String getServiceName() {
    String _switchResult = null;
    final Signature entity = this.entity;
    boolean _matched = false;
    if (!_matched) {
      if (entity instanceof OperationSignature) {
        _matched=true;
        OperationInterface _interface__OperationSignature = ((OperationSignature)this.entity).getInterface__OperationSignature();
        String _safeName = this.getSafeName(_interface__OperationSignature);
        String _firstLower = StringExtensions.toFirstLower(_safeName);
        String _plus = (_firstLower + "_");
        String _signatureName = this.getSignatureName();
        _switchResult = (_plus + _signatureName);
      }
    }
    if (!_matched) {
      if (entity instanceof InfrastructureSignature) {
        _matched=true;
        _switchResult = "";
      }
    }
    return _switchResult;
  }
  
  /**
   * Gets a unique signature name for the specified signature.
   * @return a string containing the unique name of the signature
   * @param signature the signature for which the unique name is generated
   */
  public String getSignatureName() {
    String _xblockexpression = null;
    {
      final OperationSignature signature = ((OperationSignature) this.entity);
      int position = (-1);
      OperationSignature s = null;
      do {
        {
          int _position = position;
          position = (_position + 1);
          OperationInterface _interface__OperationSignature = signature.getInterface__OperationSignature();
          EList<OperationSignature> _signatures__OperationInterface = _interface__OperationSignature.getSignatures__OperationInterface();
          OperationSignature _get = _signatures__OperationInterface.get(position);
          s = _get;
        }
      } while((!Objects.equal(s, signature)));
      String _entityName = signature.getEntityName();
      _xblockexpression = (_entityName + Integer.valueOf(position));
    }
    return _xblockexpression;
  }
}
