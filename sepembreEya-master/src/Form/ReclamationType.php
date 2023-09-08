<?php

namespace App\Form;

use App\Entity\Reclamation;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\Extension\Core\Type\TextType;

use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextareaType; 

class ReclamationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('recSubject', TextType::class, [
                'label' => 'Subject',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Write your subject'],
            ])
            ->add('recText', TextareaType::class, [
                'label' => 'Reclamation',
                'attr' => ['class' => 'message-box form-control', 
          

                    'rows' => 3, // You can adjust the number of rows as needed
                    'placeholder' => 'Enter your reclamation text here...', // Optional placeholder text
                ],
            ])
            
            
            
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Reclamation::class,
        ]);
    }
}
